import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProductInventory } from '../product-inventory.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../product-inventory.test-samples';

import { ProductInventoryService } from './product-inventory.service';

const requireRestSample: IProductInventory = {
  ...sampleWithRequiredData,
};

describe('ProductInventory Service', () => {
  let service: ProductInventoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductInventory | IProductInventory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductInventoryService);
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

    it('should create a ProductInventory', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const productInventory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productInventory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductInventory', () => {
      const productInventory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productInventory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductInventory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductInventory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductInventory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductInventoryToCollectionIfMissing', () => {
      it('should add a ProductInventory to an empty array', () => {
        const productInventory: IProductInventory = sampleWithRequiredData;
        expectedResult = service.addProductInventoryToCollectionIfMissing([], productInventory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productInventory);
      });

      it('should not add a ProductInventory to an array that contains it', () => {
        const productInventory: IProductInventory = sampleWithRequiredData;
        const productInventoryCollection: IProductInventory[] = [
          {
            ...productInventory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductInventoryToCollectionIfMissing(productInventoryCollection, productInventory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductInventory to an array that doesn't contain it", () => {
        const productInventory: IProductInventory = sampleWithRequiredData;
        const productInventoryCollection: IProductInventory[] = [sampleWithPartialData];
        expectedResult = service.addProductInventoryToCollectionIfMissing(productInventoryCollection, productInventory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productInventory);
      });

      it('should add only unique ProductInventory to an array', () => {
        const productInventoryArray: IProductInventory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productInventoryCollection: IProductInventory[] = [sampleWithRequiredData];
        expectedResult = service.addProductInventoryToCollectionIfMissing(productInventoryCollection, ...productInventoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productInventory: IProductInventory = sampleWithRequiredData;
        const productInventory2: IProductInventory = sampleWithPartialData;
        expectedResult = service.addProductInventoryToCollectionIfMissing([], productInventory, productInventory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productInventory);
        expect(expectedResult).toContain(productInventory2);
      });

      it('should accept null and undefined values', () => {
        const productInventory: IProductInventory = sampleWithRequiredData;
        expectedResult = service.addProductInventoryToCollectionIfMissing([], null, productInventory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productInventory);
      });

      it('should return initial array if no ProductInventory is added', () => {
        const productInventoryCollection: IProductInventory[] = [sampleWithRequiredData];
        expectedResult = service.addProductInventoryToCollectionIfMissing(productInventoryCollection, undefined, null);
        expect(expectedResult).toEqual(productInventoryCollection);
      });
    });

    describe('compareProductInventory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductInventory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProductInventory(entity1, entity2);
        const compareResult2 = service.compareProductInventory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProductInventory(entity1, entity2);
        const compareResult2 = service.compareProductInventory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProductInventory(entity1, entity2);
        const compareResult2 = service.compareProductInventory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
