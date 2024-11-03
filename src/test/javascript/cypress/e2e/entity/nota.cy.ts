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

describe('Nota e2e test', () => {
  const notaPageUrl = '/nota';
  const notaPageUrlPattern = new RegExp('/nota(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const notaSample = { pontuacao: 2559.49, data: '2024-11-03' };

  let nota;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/notas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/notas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/notas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (nota) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/notas/${nota.id}`,
      }).then(() => {
        nota = undefined;
      });
    }
  });

  it('Notas menu should load Notas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('nota');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Nota').should('exist');
    cy.url().should('match', notaPageUrlPattern);
  });

  describe('Nota page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(notaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Nota page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/nota/new$'));
        cy.getEntityCreateUpdateHeading('Nota');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', notaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/notas',
          body: notaSample,
        }).then(({ body }) => {
          nota = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/notas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/notas?page=0&size=20>; rel="last",<http://localhost/api/notas?page=0&size=20>; rel="first"',
              },
              body: [nota],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(notaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Nota page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('nota');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', notaPageUrlPattern);
      });

      it('edit button click should load edit Nota page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Nota');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', notaPageUrlPattern);
      });

      it('edit button click should load edit Nota page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Nota');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', notaPageUrlPattern);
      });

      it('last delete button click should delete instance of Nota', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('nota').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', notaPageUrlPattern);

        nota = undefined;
      });
    });
  });

  describe('new Nota page', () => {
    beforeEach(() => {
      cy.visit(`${notaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Nota');
    });

    it('should create an instance of Nota', () => {
      cy.get(`[data-cy="pontuacao"]`).type('12196.89');
      cy.get(`[data-cy="pontuacao"]`).should('have.value', '12196.89');

      cy.get(`[data-cy="data"]`).type('2024-11-03');
      cy.get(`[data-cy="data"]`).blur();
      cy.get(`[data-cy="data"]`).should('have.value', '2024-11-03');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        nota = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', notaPageUrlPattern);
    });
  });
});
