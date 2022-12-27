import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserAdress } from '../user-adress.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../user-adress.test-samples';

import { UserAdressService } from './user-adress.service';

const requireRestSample: IUserAdress = {
  ...sampleWithRequiredData,
};

describe('UserAdress Service', () => {
  let service: UserAdressService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserAdress | IUserAdress[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserAdressService);
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

    it('should create a UserAdress', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const userAdress = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userAdress).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserAdress', () => {
      const userAdress = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userAdress).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserAdress', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserAdress', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserAdress', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserAdressToCollectionIfMissing', () => {
      it('should add a UserAdress to an empty array', () => {
        const userAdress: IUserAdress = sampleWithRequiredData;
        expectedResult = service.addUserAdressToCollectionIfMissing([], userAdress);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAdress);
      });

      it('should not add a UserAdress to an array that contains it', () => {
        const userAdress: IUserAdress = sampleWithRequiredData;
        const userAdressCollection: IUserAdress[] = [
          {
            ...userAdress,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserAdressToCollectionIfMissing(userAdressCollection, userAdress);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserAdress to an array that doesn't contain it", () => {
        const userAdress: IUserAdress = sampleWithRequiredData;
        const userAdressCollection: IUserAdress[] = [sampleWithPartialData];
        expectedResult = service.addUserAdressToCollectionIfMissing(userAdressCollection, userAdress);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAdress);
      });

      it('should add only unique UserAdress to an array', () => {
        const userAdressArray: IUserAdress[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userAdressCollection: IUserAdress[] = [sampleWithRequiredData];
        expectedResult = service.addUserAdressToCollectionIfMissing(userAdressCollection, ...userAdressArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userAdress: IUserAdress = sampleWithRequiredData;
        const userAdress2: IUserAdress = sampleWithPartialData;
        expectedResult = service.addUserAdressToCollectionIfMissing([], userAdress, userAdress2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAdress);
        expect(expectedResult).toContain(userAdress2);
      });

      it('should accept null and undefined values', () => {
        const userAdress: IUserAdress = sampleWithRequiredData;
        expectedResult = service.addUserAdressToCollectionIfMissing([], null, userAdress, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAdress);
      });

      it('should return initial array if no UserAdress is added', () => {
        const userAdressCollection: IUserAdress[] = [sampleWithRequiredData];
        expectedResult = service.addUserAdressToCollectionIfMissing(userAdressCollection, undefined, null);
        expect(expectedResult).toEqual(userAdressCollection);
      });
    });

    describe('compareUserAdress', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserAdress(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserAdress(entity1, entity2);
        const compareResult2 = service.compareUserAdress(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserAdress(entity1, entity2);
        const compareResult2 = service.compareUserAdress(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserAdress(entity1, entity2);
        const compareResult2 = service.compareUserAdress(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
