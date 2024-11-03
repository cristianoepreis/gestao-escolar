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

describe('Disciplina e2e test', () => {
  const disciplinaPageUrl = '/disciplina';
  const disciplinaPageUrlPattern = new RegExp('/disciplina(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const disciplinaSample = { nome: 'triumphantly premium sleepily', codigo: 'annually whoever powerfully', cargaHoraria: 28359 };

  let disciplina;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/disciplinas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/disciplinas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/disciplinas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (disciplina) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/disciplinas/${disciplina.id}`,
      }).then(() => {
        disciplina = undefined;
      });
    }
  });

  it('Disciplinas menu should load Disciplinas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('disciplina');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Disciplina').should('exist');
    cy.url().should('match', disciplinaPageUrlPattern);
  });

  describe('Disciplina page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(disciplinaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Disciplina page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/disciplina/new$'));
        cy.getEntityCreateUpdateHeading('Disciplina');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplinaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/disciplinas',
          body: disciplinaSample,
        }).then(({ body }) => {
          disciplina = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/disciplinas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/disciplinas?page=0&size=20>; rel="last",<http://localhost/api/disciplinas?page=0&size=20>; rel="first"',
              },
              body: [disciplina],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(disciplinaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Disciplina page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('disciplina');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplinaPageUrlPattern);
      });

      it('edit button click should load edit Disciplina page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Disciplina');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplinaPageUrlPattern);
      });

      it('edit button click should load edit Disciplina page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Disciplina');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplinaPageUrlPattern);
      });

      it('last delete button click should delete instance of Disciplina', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('disciplina').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplinaPageUrlPattern);

        disciplina = undefined;
      });
    });
  });

  describe('new Disciplina page', () => {
    beforeEach(() => {
      cy.visit(`${disciplinaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Disciplina');
    });

    it('should create an instance of Disciplina', () => {
      cy.get(`[data-cy="nome"]`).type('likewise accidentally');
      cy.get(`[data-cy="nome"]`).should('have.value', 'likewise accidentally');

      cy.get(`[data-cy="codigo"]`).type('blah though');
      cy.get(`[data-cy="codigo"]`).should('have.value', 'blah though');

      cy.get(`[data-cy="cargaHoraria"]`).type('26156');
      cy.get(`[data-cy="cargaHoraria"]`).should('have.value', '26156');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        disciplina = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', disciplinaPageUrlPattern);
    });
  });
});
