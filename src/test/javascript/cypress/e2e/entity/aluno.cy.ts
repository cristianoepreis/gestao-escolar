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

describe('Aluno e2e test', () => {
  const alunoPageUrl = '/aluno';
  const alunoPageUrlPattern = new RegExp('/aluno(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const alunoSample = { nome: 'ugh', dataDeNascimento: '2024-11-03', cpf: 'smoothly meh about', email: 'Isabela.Silva46@hotmail.com' };

  let aluno;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/alunos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/alunos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/alunos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (aluno) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/alunos/${aluno.id}`,
      }).then(() => {
        aluno = undefined;
      });
    }
  });

  it('Alunos menu should load Alunos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('aluno');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Aluno').should('exist');
    cy.url().should('match', alunoPageUrlPattern);
  });

  describe('Aluno page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(alunoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Aluno page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/aluno/new$'));
        cy.getEntityCreateUpdateHeading('Aluno');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', alunoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/alunos',
          body: alunoSample,
        }).then(({ body }) => {
          aluno = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/alunos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/alunos?page=0&size=20>; rel="last",<http://localhost/api/alunos?page=0&size=20>; rel="first"',
              },
              body: [aluno],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(alunoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Aluno page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('aluno');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', alunoPageUrlPattern);
      });

      it('edit button click should load edit Aluno page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Aluno');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', alunoPageUrlPattern);
      });

      it('edit button click should load edit Aluno page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Aluno');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', alunoPageUrlPattern);
      });

      it('last delete button click should delete instance of Aluno', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('aluno').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', alunoPageUrlPattern);

        aluno = undefined;
      });
    });
  });

  describe('new Aluno page', () => {
    beforeEach(() => {
      cy.visit(`${alunoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Aluno');
    });

    it('should create an instance of Aluno', () => {
      cy.get(`[data-cy="nome"]`).type('put likewise voluntarily');
      cy.get(`[data-cy="nome"]`).should('have.value', 'put likewise voluntarily');

      cy.get(`[data-cy="dataDeNascimento"]`).type('2024-11-03');
      cy.get(`[data-cy="dataDeNascimento"]`).blur();
      cy.get(`[data-cy="dataDeNascimento"]`).should('have.value', '2024-11-03');

      cy.get(`[data-cy="cpf"]`).type('curiously browse continually');
      cy.get(`[data-cy="cpf"]`).should('have.value', 'curiously browse continually');

      cy.get(`[data-cy="endereco"]`).type('beside really wearily');
      cy.get(`[data-cy="endereco"]`).should('have.value', 'beside really wearily');

      cy.get(`[data-cy="telefone"]`).type('yowza woot gosh');
      cy.get(`[data-cy="telefone"]`).should('have.value', 'yowza woot gosh');

      cy.get(`[data-cy="email"]`).type('Bryan.Oliveira18@hotmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Bryan.Oliveira18@hotmail.com');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        aluno = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', alunoPageUrlPattern);
    });
  });
});
