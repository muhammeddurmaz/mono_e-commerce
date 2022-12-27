import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProductDiscount } from '../product-discount.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../product-discount.test-samples';

import { ProductDiscountService, RestProductDiscount } from './product-discount.service';

const requireRestSample: RestProductDiscount = {
  ...sampleWithRequiredData,
  addedDate: sampleWithRequiredData.addedDate?.toJSON(),
  dueDate: sampleWithRequiredData.dueDate?.toJSON(),
};

describe('ProductDiscount Service', () => {
  let service: ProductDiscountService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductDiscount | IProductDiscount[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductDiscountService);
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

    it('should create a ProductDiscount', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const productDiscount = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productDiscount).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductDiscount', () => {
      const productDiscount = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productDiscount).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductDiscount', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductDiscount', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductDiscount', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductDiscountToCollectionIfMissing', () => {
      it('should add a ProductDiscount to an empty array', () => {
        const productDiscount: IProductDiscount = sampleWithRequiredData;
        expectedResult = service.addProductDiscountToCollectionIfMissing([], productDiscount);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productDiscount);
      });

      it('should not add a ProductDiscount to an array that contains it', () => {
        const productDiscount: IProductDiscount = sampleWithRequiredData;
        const productDiscountCollection: IProductDiscount[] = [
          {
            ...productDiscount,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductDiscountToCollectionIfMissing(productDiscountCollection, productDiscount);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductDiscount to an array that doesn't contain it", () => {
        const productDiscount: IProductDiscount = sampleWithRequiredData;
        const productDiscountCollection: IProductDiscount[] = [sampleWithPartialData];
        expectedResult = service.addProductDiscountToCollectionIfMissing(productDiscountCollection, productDiscount);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productDiscount);
      });

      it('should add only unique ProductDiscount to an array', () => {
        const productDiscountArray: IProductDiscount[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productDiscountCollection: IProductDiscount[] = [sampleWithRequiredData];
        expectedResult = service.addProductDiscountToCollectionIfMissing(productDiscountCollection, ...productDiscountArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productDiscount: IProductDiscount = sampleWithRequiredData;
        const productDiscount2: IProductDiscount = sampleWithPartialData;
        expectedResult = service.addProductDiscountToCollectionIfMissing([], productDiscount, productDiscount2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productDiscount);
        expect(expectedResult).toContain(productDiscount2);
      });

      it('should accept null and undefined values', () => {
        const productDiscount: IProductDiscount = sampleWithRequiredData;
        expectedResult = service.addProductDiscountToCollectionIfMissing([], null, productDiscount, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productDiscount);
      });

      it('should return initial array if no ProductDiscount is added', () => {
        const productDiscountCollection: IProductDiscount[] = [sampleWithRequiredData];
        expectedResult = service.addProductDiscountToCollectionIfMissing(productDiscountCollection, undefined, null);
        expect(expectedResult).toEqual(productDiscountCollection);
      });
    });

    describe('compareProductDiscount', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductDiscount(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProductDiscount(entity1, entity2);
        const compareResult2 = service.compareProductDiscount(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProductDiscount(entity1, entity2);
        const compareResult2 = service.compareProductDiscount(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProductDiscount(entity1, entity2);
        const compareResult2 = service.compareProductDiscount(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
