import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDisciplina } from '../disciplina.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../disciplina.test-samples';

import { DisciplinaService } from './disciplina.service';

const requireRestSample: IDisciplina = {
  ...sampleWithRequiredData,
};

describe('Disciplina Service', () => {
  let service: DisciplinaService;
  let httpMock: HttpTestingController;
  let expectedResult: IDisciplina | IDisciplina[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DisciplinaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Disciplina', () => {
      const disciplina = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(disciplina).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Disciplina', () => {
      const disciplina = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(disciplina).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Disciplina', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Disciplina', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Disciplina', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDisciplinaToCollectionIfMissing', () => {
      it('should add a Disciplina to an empty array', () => {
        const disciplina: IDisciplina = sampleWithRequiredData;
        expectedResult = service.addDisciplinaToCollectionIfMissing([], disciplina);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(disciplina);
      });

      it('should not add a Disciplina to an array that contains it', () => {
        const disciplina: IDisciplina = sampleWithRequiredData;
        const disciplinaCollection: IDisciplina[] = [
          {
            ...disciplina,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDisciplinaToCollectionIfMissing(disciplinaCollection, disciplina);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Disciplina to an array that doesn't contain it", () => {
        const disciplina: IDisciplina = sampleWithRequiredData;
        const disciplinaCollection: IDisciplina[] = [sampleWithPartialData];
        expectedResult = service.addDisciplinaToCollectionIfMissing(disciplinaCollection, disciplina);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(disciplina);
      });

      it('should add only unique Disciplina to an array', () => {
        const disciplinaArray: IDisciplina[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const disciplinaCollection: IDisciplina[] = [sampleWithRequiredData];
        expectedResult = service.addDisciplinaToCollectionIfMissing(disciplinaCollection, ...disciplinaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const disciplina: IDisciplina = sampleWithRequiredData;
        const disciplina2: IDisciplina = sampleWithPartialData;
        expectedResult = service.addDisciplinaToCollectionIfMissing([], disciplina, disciplina2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(disciplina);
        expect(expectedResult).toContain(disciplina2);
      });

      it('should accept null and undefined values', () => {
        const disciplina: IDisciplina = sampleWithRequiredData;
        expectedResult = service.addDisciplinaToCollectionIfMissing([], null, disciplina, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(disciplina);
      });

      it('should return initial array if no Disciplina is added', () => {
        const disciplinaCollection: IDisciplina[] = [sampleWithRequiredData];
        expectedResult = service.addDisciplinaToCollectionIfMissing(disciplinaCollection, undefined, null);
        expect(expectedResult).toEqual(disciplinaCollection);
      });
    });

    describe('compareDisciplina', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDisciplina(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDisciplina(entity1, entity2);
        const compareResult2 = service.compareDisciplina(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDisciplina(entity1, entity2);
        const compareResult2 = service.compareDisciplina(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDisciplina(entity1, entity2);
        const compareResult2 = service.compareDisciplina(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
