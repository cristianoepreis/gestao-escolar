import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Matricula e2e test', () => {
  const matriculaPageUrl = '/matricula';
  const matriculaPageUrlPattern = new RegExp('/matricula(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const matriculaSample = { dataDeMatricula: '2024-11-03', status: 'ATIVO' };

  let matricula;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/matriculas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/matriculas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/matriculas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (matricula) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/matriculas/${matricula.id}`,
      }).then(() => {
        matricula = undefined;
      });
    }
  });

  it('Matriculas menu should load Matriculas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('matricula');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Matricula').should('exist');
    cy.url().should('match', matriculaPageUrlPattern);
  });

  describe('Matricula page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(matriculaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Matricula page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/matricula/new$'));
        cy.getEntityCreateUpdateHeading('Matricula');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', matriculaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/matriculas',
          body: matriculaSample,
        }).then(({ body }) => {
          matricula = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/matriculas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/matriculas?page=0&size=20>; rel="last",<http://localhost/api/matriculas?page=0&size=20>; rel="first"',
              },
              body: [matricula],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(matriculaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Matricula page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('matricula');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', matriculaPageUrlPattern);
      });

      it('edit button click should load edit Matricula page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Matricula');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', matriculaPageUrlPattern);
      });

      it('edit button click should load edit Matricula page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Matricula');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', matriculaPageUrlPattern);
      });

      it('last delete button click should delete instance of Matricula', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('matricula').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', matriculaPageUrlPattern);

        matricula = undefined;
      });
    });
  });

  describe('new Matricula page', () => {
    beforeEach(() => {
      cy.visit(`${matriculaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Matricula');
    });

    it('should create an instance of Matricula', () => {
      cy.get(`[data-cy="dataDeMatricula"]`).type('2024-11-03');
      cy.get(`[data-cy="dataDeMatricula"]`).blur();
      cy.get(`[data-cy="dataDeMatricula"]`).should('have.value', '2024-11-03');

      cy.get(`[data-cy="status"]`).select('TRANCADO');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        matricula = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', matriculaPageUrlPattern);
    });
  });
});
