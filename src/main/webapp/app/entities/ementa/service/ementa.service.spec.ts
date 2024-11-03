import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IEmenta } from '../ementa.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ementa.test-samples';

import { EmentaService } from './ementa.service';

const requireRestSample: IEmenta = {
  ...sampleWithRequiredData,
};

describe('Ementa Service', () => {
  let service: EmentaService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmenta | IEmenta[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EmentaService);
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

    it('should create a Ementa', () => {
      const ementa = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ementa).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ementa', () => {
      const ementa = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ementa).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ementa', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ementa', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Ementa', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmentaToCollectionIfMissing', () => {
      it('should add a Ementa to an empty array', () => {
        const ementa: IEmenta = sampleWithRequiredData;
        expectedResult = service.addEmentaToCollectionIfMissing([], ementa);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ementa);
      });

      it('should not add a Ementa to an array that contains it', () => {
        const ementa: IEmenta = sampleWithRequiredData;
        const ementaCollection: IEmenta[] = [
          {
            ...ementa,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmentaToCollectionIfMissing(ementaCollection, ementa);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ementa to an array that doesn't contain it", () => {
        const ementa: IEmenta = sampleWithRequiredData;
        const ementaCollection: IEmenta[] = [sampleWithPartialData];
        expectedResult = service.addEmentaToCollectionIfMissing(ementaCollection, ementa);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ementa);
      });

      it('should add only unique Ementa to an array', () => {
        const ementaArray: IEmenta[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ementaCollection: IEmenta[] = [sampleWithRequiredData];
        expectedResult = service.addEmentaToCollectionIfMissing(ementaCollection, ...ementaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ementa: IEmenta = sampleWithRequiredData;
        const ementa2: IEmenta = sampleWithPartialData;
        expectedResult = service.addEmentaToCollectionIfMissing([], ementa, ementa2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ementa);
        expect(expectedResult).toContain(ementa2);
      });

      it('should accept null and undefined values', () => {
        const ementa: IEmenta = sampleWithRequiredData;
        expectedResult = service.addEmentaToCollectionIfMissing([], null, ementa, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ementa);
      });

      it('should return initial array if no Ementa is added', () => {
        const ementaCollection: IEmenta[] = [sampleWithRequiredData];
        expectedResult = service.addEmentaToCollectionIfMissing(ementaCollection, undefined, null);
        expect(expectedResult).toEqual(ementaCollection);
      });
    });

    describe('compareEmenta', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmenta(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmenta(entity1, entity2);
        const compareResult2 = service.compareEmenta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmenta(entity1, entity2);
        const compareResult2 = service.compareEmenta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmenta(entity1, entity2);
        const compareResult2 = service.compareEmenta(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
