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

describe('Curso e2e test', () => {
  const cursoPageUrl = '/curso';
  const cursoPageUrlPattern = new RegExp('/curso(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const cursoSample = { nome: 'cellar bah', duracao: 28297 };

  let curso;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cursos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cursos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cursos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (curso) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cursos/${curso.id}`,
      }).then(() => {
        curso = undefined;
      });
    }
  });

  it('Cursos menu should load Cursos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('curso');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Curso').should('exist');
    cy.url().should('match', cursoPageUrlPattern);
  });

  describe('Curso page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cursoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Curso page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/curso/new$'));
        cy.getEntityCreateUpdateHeading('Curso');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cursoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cursos',
          body: cursoSample,
        }).then(({ body }) => {
          curso = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cursos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cursos?page=0&size=20>; rel="last",<http://localhost/api/cursos?page=0&size=20>; rel="first"',
              },
              body: [curso],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(cursoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Curso page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('curso');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cursoPageUrlPattern);
      });

      it('edit button click should load edit Curso page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Curso');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cursoPageUrlPattern);
      });

      it('edit button click should load edit Curso page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Curso');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cursoPageUrlPattern);
      });

      it('last delete button click should delete instance of Curso', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('curso').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cursoPageUrlPattern);

        curso = undefined;
      });
    });
  });

  describe('new Curso page', () => {
    beforeEach(() => {
      cy.visit(`${cursoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Curso');
    });

    it('should create an instance of Curso', () => {
      cy.get(`[data-cy="nome"]`).type('hm outset');
      cy.get(`[data-cy="nome"]`).should('have.value', 'hm outset');

      cy.get(`[data-cy="descricao"]`).type('apud whether until');
      cy.get(`[data-cy="descricao"]`).should('have.value', 'apud whether until');

      cy.get(`[data-cy="duracao"]`).type('6422');
      cy.get(`[data-cy="duracao"]`).should('have.value', '6422');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        curso = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', cursoPageUrlPattern);
    });
  });
});
