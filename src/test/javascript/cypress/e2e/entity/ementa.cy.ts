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

describe('Ementa e2e test', () => {
  const ementaPageUrl = '/ementa';
  const ementaPageUrlPattern = new RegExp('/ementa(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ementaSample = {};

  let ementa;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ementas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ementas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ementas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ementa) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ementas/${ementa.id}`,
      }).then(() => {
        ementa = undefined;
      });
    }
  });

  it('Ementas menu should load Ementas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ementa');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Ementa').should('exist');
    cy.url().should('match', ementaPageUrlPattern);
  });

  describe('Ementa page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ementaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Ementa page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ementa/new$'));
        cy.getEntityCreateUpdateHeading('Ementa');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ementaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ementas',
          body: ementaSample,
        }).then(({ body }) => {
          ementa = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ementas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/ementas?page=0&size=20>; rel="last",<http://localhost/api/ementas?page=0&size=20>; rel="first"',
              },
              body: [ementa],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ementaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Ementa page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ementa');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ementaPageUrlPattern);
      });

      it('edit button click should load edit Ementa page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ementa');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ementaPageUrlPattern);
      });

      it('edit button click should load edit Ementa page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ementa');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ementaPageUrlPattern);
      });

      it('last delete button click should delete instance of Ementa', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ementa').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ementaPageUrlPattern);

        ementa = undefined;
      });
    });
  });

  describe('new Ementa page', () => {
    beforeEach(() => {
      cy.visit(`${ementaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Ementa');
    });

    it('should create an instance of Ementa', () => {
      cy.get(`[data-cy="descricao"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="descricao"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="bibliografiaBasica"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="bibliografiaBasica"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="bibliografiaComplementar"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="bibliografiaComplementar"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="praticaLaboratorial"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="praticaLaboratorial"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        ementa = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', ementaPageUrlPattern);
    });
  });
});
