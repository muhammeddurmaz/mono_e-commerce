import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProductModel } from '../product-model.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../product-model.test-samples';

import { ProductModelService } from './product-model.service';

const requireRestSample: IProductModel = {
  ...sampleWithRequiredData,
};

describe('ProductModel Service', () => {
  let service: ProductModelService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductModel | IProductModel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductModelService);
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

    it('should create a ProductModel', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const productModel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productModel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductModel', () => {
      const productModel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productModel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductModel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductModel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductModel', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductModelToCollectionIfMissing', () => {
      it('should add a ProductModel to an empty array', () => {
        const productModel: IProductModel = sampleWithRequiredData;
        expectedResult = service.addProductModelToCollectionIfMissing([], productModel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productModel);
      });

      it('should not add a ProductModel to an array that contains it', () => {
        const productModel: IProductModel = sampleWithRequiredData;
        const productModelCollection: IProductModel[] = [
          {
            ...productModel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductModelToCollectionIfMissing(productModelCollection, productModel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductModel to an array that doesn't contain it", () => {
        const productModel: IProductModel = sampleWithRequiredData;
        const productModelCollection: IProductModel[] = [sampleWithPartialData];
        expectedResult = service.addProductModelToCollectionIfMissing(productModelCollection, productModel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productModel);
      });

      it('should add only unique ProductModel to an array', () => {
        const productModelArray: IProductModel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productModelCollection: IProductModel[] = [sampleWithRequiredData];
        expectedResult = service.addProductModelToCollectionIfMissing(productModelCollection, ...productModelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productModel: IProductModel = sampleWithRequiredData;
        const productModel2: IProductModel = sampleWithPartialData;
        expectedResult = service.addProductModelToCollectionIfMissing([], productModel, productModel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productModel);
        expect(expectedResult).toContain(productModel2);
      });

      it('should accept null and undefined values', () => {
        const productModel: IProductModel = sampleWithRequiredData;
        expectedResult = service.addProductModelToCollectionIfMissing([], null, productModel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productModel);
      });

      it('should return initial array if no ProductModel is added', () => {
        const productModelCollection: IProductModel[] = [sampleWithRequiredData];
        expectedResult = service.addProductModelToCollectionIfMissing(productModelCollection, undefined, null);
        expect(expectedResult).toEqual(productModelCollection);
      });
    });

    describe('compareProductModel', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductModel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProductModel(entity1, entity2);
        const compareResult2 = service.compareProductModel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProductModel(entity1, entity2);
        const compareResult2 = service.compareProductModel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProductModel(entity1, entity2);
        const compareResult2 = service.compareProductModel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
