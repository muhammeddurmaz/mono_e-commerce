import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISellerStatistics } from '../seller-statistics.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../seller-statistics.test-samples';

import { SellerStatisticsService } from './seller-statistics.service';

const requireRestSample: ISellerStatistics = {
  ...sampleWithRequiredData,
};

describe('SellerStatistics Service', () => {
  let service: SellerStatisticsService;
  let httpMock: HttpTestingController;
  let expectedResult: ISellerStatistics | ISellerStatistics[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SellerStatisticsService);
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

    it('should return a list of SellerStatistics', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    describe('addSellerStatisticsToCollectionIfMissing', () => {
      it('should add a SellerStatistics to an empty array', () => {
        const sellerStatistics: ISellerStatistics = sampleWithRequiredData;
        expectedResult = service.addSellerStatisticsToCollectionIfMissing([], sellerStatistics);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sellerStatistics);
      });

      it('should not add a SellerStatistics to an array that contains it', () => {
        const sellerStatistics: ISellerStatistics = sampleWithRequiredData;
        const sellerStatisticsCollection: ISellerStatistics[] = [
          {
            ...sellerStatistics,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSellerStatisticsToCollectionIfMissing(sellerStatisticsCollection, sellerStatistics);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SellerStatistics to an array that doesn't contain it", () => {
        const sellerStatistics: ISellerStatistics = sampleWithRequiredData;
        const sellerStatisticsCollection: ISellerStatistics[] = [sampleWithPartialData];
        expectedResult = service.addSellerStatisticsToCollectionIfMissing(sellerStatisticsCollection, sellerStatistics);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sellerStatistics);
      });

      it('should add only unique SellerStatistics to an array', () => {
        const sellerStatisticsArray: ISellerStatistics[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sellerStatisticsCollection: ISellerStatistics[] = [sampleWithRequiredData];
        expectedResult = service.addSellerStatisticsToCollectionIfMissing(sellerStatisticsCollection, ...sellerStatisticsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sellerStatistics: ISellerStatistics = sampleWithRequiredData;
        const sellerStatistics2: ISellerStatistics = sampleWithPartialData;
        expectedResult = service.addSellerStatisticsToCollectionIfMissing([], sellerStatistics, sellerStatistics2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sellerStatistics);
        expect(expectedResult).toContain(sellerStatistics2);
      });

      it('should accept null and undefined values', () => {
        const sellerStatistics: ISellerStatistics = sampleWithRequiredData;
        expectedResult = service.addSellerStatisticsToCollectionIfMissing([], null, sellerStatistics, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sellerStatistics);
      });

      it('should return initial array if no SellerStatistics is added', () => {
        const sellerStatisticsCollection: ISellerStatistics[] = [sampleWithRequiredData];
        expectedResult = service.addSellerStatisticsToCollectionIfMissing(sellerStatisticsCollection, undefined, null);
        expect(expectedResult).toEqual(sellerStatisticsCollection);
      });
    });

    describe('compareSellerStatistics', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSellerStatistics(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSellerStatistics(entity1, entity2);
        const compareResult2 = service.compareSellerStatistics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSellerStatistics(entity1, entity2);
        const compareResult2 = service.compareSellerStatistics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSellerStatistics(entity1, entity2);
        const compareResult2 = service.compareSellerStatistics(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
