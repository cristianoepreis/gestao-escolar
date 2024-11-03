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

describe('Administrador e2e test', () => {
  const administradorPageUrl = '/administrador';
  const administradorPageUrlPattern = new RegExp('/administrador(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const administradorSample = { nome: 'healthily incandescence as', email: 'Mercia.Silva@hotmail.com' };

  let administrador;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/administradors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/administradors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/administradors/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (administrador) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/administradors/${administrador.id}`,
      }).then(() => {
        administrador = undefined;
      });
    }
  });

  it('Administradors menu should load Administradors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('administrador');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Administrador').should('exist');
    cy.url().should('match', administradorPageUrlPattern);
  });

  describe('Administrador page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(administradorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Administrador page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/administrador/new$'));
        cy.getEntityCreateUpdateHeading('Administrador');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', administradorPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/administradors',
          body: administradorSample,
        }).then(({ body }) => {
          administrador = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/administradors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/administradors?page=0&size=20>; rel="last",<http://localhost/api/administradors?page=0&size=20>; rel="first"',
              },
              body: [administrador],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(administradorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Administrador page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('administrador');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', administradorPageUrlPattern);
      });

      it('edit button click should load edit Administrador page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Administrador');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', administradorPageUrlPattern);
      });

      it('edit button click should load edit Administrador page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Administrador');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', administradorPageUrlPattern);
      });

      it('last delete button click should delete instance of Administrador', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('administrador').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', administradorPageUrlPattern);

        administrador = undefined;
      });
    });
  });

  describe('new Administrador page', () => {
    beforeEach(() => {
      cy.visit(`${administradorPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Administrador');
    });

    it('should create an instance of Administrador', () => {
      cy.get(`[data-cy="nome"]`).type('wombat');
      cy.get(`[data-cy="nome"]`).should('have.value', 'wombat');

      cy.get(`[data-cy="telefone"]`).type('via sizzling dwell');
      cy.get(`[data-cy="telefone"]`).should('have.value', 'via sizzling dwell');

      cy.get(`[data-cy="email"]`).type('Eduarda.Carvalho28@gmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Eduarda.Carvalho28@gmail.com');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        administrador = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', administradorPageUrlPattern);
    });
  });
});
