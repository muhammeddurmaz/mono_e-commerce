import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProductStatistics } from '../product-statistics.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../product-statistics.test-samples';

import { ProductStatisticsService } from './product-statistics.service';

const requireRestSample: IProductStatistics = {
  ...sampleWithRequiredData,
};

describe('ProductStatistics Service', () => {
  let service: ProductStatisticsService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductStatistics | IProductStatistics[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductStatisticsService);
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

    it('should return a list of ProductStatistics', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    describe('addProductStatisticsToCollectionIfMissing', () => {
      it('should add a ProductStatistics to an empty array', () => {
        const productStatistics: IProductStatistics = sampleWithRequiredData;
        expectedResult = service.addProductStatisticsToCollectionIfMissing([], productStatistics);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productStatistics);
      });

      it('should not add a ProductStatistics to an array that contains it', () => {
        const productStatistics: IProductStatistics = sampleWithRequiredData;
        const productStatisticsCollection: IProductStatistics[] = [
          {
            ...productStatistics,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductStatisticsToCollectionIfMissing(productStatisticsCollection, productStatistics);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductStatistics to an array that doesn't contain it", () => {
        const productStatistics: IProductStatistics = sampleWithRequiredData;
        const productStatisticsCollection: IProductStatistics[] = [sampleWithPartialData];
        expectedResult = service.addProductStatisticsToCollectionIfMissing(productStatisticsCollection, productStatistics);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productStatistics);
      });

      it('should add only unique ProductStatistics to an array', () => {
        const productStatisticsArray: IProductStatistics[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productStatisticsCollection: IProductStatistics[] = [sampleWithRequiredData];
        expectedResult = service.addProductStatisticsToCollectionIfMissing(productStatisticsCollection, ...productStatisticsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productStatistics: IProductStatistics = sampleWithRequiredData;
        const productStatistics2: IProductStatistics = sampleWithPartialData;
        expectedResult = service.addProductStatisticsToCollectionIfMissing([], productStatistics, productStatistics2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productStatistics);
        expect(expectedResult).toContain(productStatistics2);
      });

      it('should accept null and undefined values', () => {
        const productStatistics: IProductStatistics = sampleWithRequiredData;
        expectedResult = service.addProductStatisticsToCollectionIfMissing([], null, productStatistics, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productStatistics);
      });

      it('should return initial array if no ProductStatistics is added', () => {
        const productStatisticsCollection: IProductStatistics[] = [sampleWithRequiredData];
        expectedResult = service.addProductStatisticsToCollectionIfMissing(productStatisticsCollection, undefined, null);
        expect(expectedResult).toEqual(productStatisticsCollection);
      });
    });

    describe('compareProductStatistics', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductStatistics(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProductStatistics(entity1, entity2);
        const compareResult2 = service.compareProductStatistics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProductStatistics(entity1, entity2);
        const compareResult2 = service.compareProductStatistics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProductStatistics(entity1, entity2);
        const compareResult2 = service.compareProductStatistics(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
