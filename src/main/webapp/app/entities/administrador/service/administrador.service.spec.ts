import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAdministrador } from '../administrador.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../administrador.test-samples';

import { AdministradorService } from './administrador.service';

const requireRestSample: IAdministrador = {
  ...sampleWithRequiredData,
};

describe('Administrador Service', () => {
  let service: AdministradorService;
  let httpMock: HttpTestingController;
  let expectedResult: IAdministrador | IAdministrador[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AdministradorService);
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

    it('should create a Administrador', () => {
      const administrador = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(administrador).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Administrador', () => {
      const administrador = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(administrador).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Administrador', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Administrador', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Administrador', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAdministradorToCollectionIfMissing', () => {
      it('should add a Administrador to an empty array', () => {
        const administrador: IAdministrador = sampleWithRequiredData;
        expectedResult = service.addAdministradorToCollectionIfMissing([], administrador);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(administrador);
      });

      it('should not add a Administrador to an array that contains it', () => {
        const administrador: IAdministrador = sampleWithRequiredData;
        const administradorCollection: IAdministrador[] = [
          {
            ...administrador,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAdministradorToCollectionIfMissing(administradorCollection, administrador);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Administrador to an array that doesn't contain it", () => {
        const administrador: IAdministrador = sampleWithRequiredData;
        const administradorCollection: IAdministrador[] = [sampleWithPartialData];
        expectedResult = service.addAdministradorToCollectionIfMissing(administradorCollection, administrador);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(administrador);
      });

      it('should add only unique Administrador to an array', () => {
        const administradorArray: IAdministrador[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const administradorCollection: IAdministrador[] = [sampleWithRequiredData];
        expectedResult = service.addAdministradorToCollectionIfMissing(administradorCollection, ...administradorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const administrador: IAdministrador = sampleWithRequiredData;
        const administrador2: IAdministrador = sampleWithPartialData;
        expectedResult = service.addAdministradorToCollectionIfMissing([], administrador, administrador2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(administrador);
        expect(expectedResult).toContain(administrador2);
      });

      it('should accept null and undefined values', () => {
        const administrador: IAdministrador = sampleWithRequiredData;
        expectedResult = service.addAdministradorToCollectionIfMissing([], null, administrador, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(administrador);
      });

      it('should return initial array if no Administrador is added', () => {
        const administradorCollection: IAdministrador[] = [sampleWithRequiredData];
        expectedResult = service.addAdministradorToCollectionIfMissing(administradorCollection, undefined, null);
        expect(expectedResult).toEqual(administradorCollection);
      });
    });

    describe('compareAdministrador', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAdministrador(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAdministrador(entity1, entity2);
        const compareResult2 = service.compareAdministrador(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAdministrador(entity1, entity2);
        const compareResult2 = service.compareAdministrador(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAdministrador(entity1, entity2);
        const compareResult2 = service.compareAdministrador(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
