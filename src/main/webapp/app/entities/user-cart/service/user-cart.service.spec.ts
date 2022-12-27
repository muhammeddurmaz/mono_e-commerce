import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserCart } from '../user-cart.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../user-cart.test-samples';

import { UserCartService } from './user-cart.service';

const requireRestSample: IUserCart = {
  ...sampleWithRequiredData,
};

describe('UserCart Service', () => {
  let service: UserCartService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserCart | IUserCart[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserCartService);
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

    it('should create a UserCart', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const userCart = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userCart).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserCart', () => {
      const userCart = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userCart).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserCart', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserCart', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserCart', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserCartToCollectionIfMissing', () => {
      it('should add a UserCart to an empty array', () => {
        const userCart: IUserCart = sampleWithRequiredData;
        expectedResult = service.addUserCartToCollectionIfMissing([], userCart);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userCart);
      });

      it('should not add a UserCart to an array that contains it', () => {
        const userCart: IUserCart = sampleWithRequiredData;
        const userCartCollection: IUserCart[] = [
          {
            ...userCart,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserCartToCollectionIfMissing(userCartCollection, userCart);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserCart to an array that doesn't contain it", () => {
        const userCart: IUserCart = sampleWithRequiredData;
        const userCartCollection: IUserCart[] = [sampleWithPartialData];
        expectedResult = service.addUserCartToCollectionIfMissing(userCartCollection, userCart);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userCart);
      });

      it('should add only unique UserCart to an array', () => {
        const userCartArray: IUserCart[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userCartCollection: IUserCart[] = [sampleWithRequiredData];
        expectedResult = service.addUserCartToCollectionIfMissing(userCartCollection, ...userCartArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userCart: IUserCart = sampleWithRequiredData;
        const userCart2: IUserCart = sampleWithPartialData;
        expectedResult = service.addUserCartToCollectionIfMissing([], userCart, userCart2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userCart);
        expect(expectedResult).toContain(userCart2);
      });

      it('should accept null and undefined values', () => {
        const userCart: IUserCart = sampleWithRequiredData;
        expectedResult = service.addUserCartToCollectionIfMissing([], null, userCart, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userCart);
      });

      it('should return initial array if no UserCart is added', () => {
        const userCartCollection: IUserCart[] = [sampleWithRequiredData];
        expectedResult = service.addUserCartToCollectionIfMissing(userCartCollection, undefined, null);
        expect(expectedResult).toEqual(userCartCollection);
      });
    });

    describe('compareUserCart', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserCart(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserCart(entity1, entity2);
        const compareResult2 = service.compareUserCart(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserCart(entity1, entity2);
        const compareResult2 = service.compareUserCart(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserCart(entity1, entity2);
        const compareResult2 = service.compareUserCart(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
