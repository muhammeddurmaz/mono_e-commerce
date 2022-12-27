import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPropertyDes } from '../property-des.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../property-des.test-samples';

import { PropertyDesService } from './property-des.service';

const requireRestSample: IPropertyDes = {
  ...sampleWithRequiredData,
};

describe('PropertyDes Service', () => {
  let service: PropertyDesService;
  let httpMock: HttpTestingController;
  let expectedResult: IPropertyDes | IPropertyDes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PropertyDesService);
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

    it('should create a PropertyDes', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const propertyDes = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(propertyDes).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PropertyDes', () => {
      const propertyDes = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(propertyDes).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PropertyDes', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PropertyDes', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PropertyDes', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPropertyDesToCollectionIfMissing', () => {
      it('should add a PropertyDes to an empty array', () => {
        const propertyDes: IPropertyDes = sampleWithRequiredData;
        expectedResult = service.addPropertyDesToCollectionIfMissing([], propertyDes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(propertyDes);
      });

      it('should not add a PropertyDes to an array that contains it', () => {
        const propertyDes: IPropertyDes = sampleWithRequiredData;
        const propertyDesCollection: IPropertyDes[] = [
          {
            ...propertyDes,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPropertyDesToCollectionIfMissing(propertyDesCollection, propertyDes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PropertyDes to an array that doesn't contain it", () => {
        const propertyDes: IPropertyDes = sampleWithRequiredData;
        const propertyDesCollection: IPropertyDes[] = [sampleWithPartialData];
        expectedResult = service.addPropertyDesToCollectionIfMissing(propertyDesCollection, propertyDes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(propertyDes);
      });

      it('should add only unique PropertyDes to an array', () => {
        const propertyDesArray: IPropertyDes[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const propertyDesCollection: IPropertyDes[] = [sampleWithRequiredData];
        expectedResult = service.addPropertyDesToCollectionIfMissing(propertyDesCollection, ...propertyDesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const propertyDes: IPropertyDes = sampleWithRequiredData;
        const propertyDes2: IPropertyDes = sampleWithPartialData;
        expectedResult = service.addPropertyDesToCollectionIfMissing([], propertyDes, propertyDes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(propertyDes);
        expect(expectedResult).toContain(propertyDes2);
      });

      it('should accept null and undefined values', () => {
        const propertyDes: IPropertyDes = sampleWithRequiredData;
        expectedResult = service.addPropertyDesToCollectionIfMissing([], null, propertyDes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(propertyDes);
      });

      it('should return initial array if no PropertyDes is added', () => {
        const propertyDesCollection: IPropertyDes[] = [sampleWithRequiredData];
        expectedResult = service.addPropertyDesToCollectionIfMissing(propertyDesCollection, undefined, null);
        expect(expectedResult).toEqual(propertyDesCollection);
      });
    });

    describe('comparePropertyDes', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePropertyDes(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePropertyDes(entity1, entity2);
        const compareResult2 = service.comparePropertyDes(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePropertyDes(entity1, entity2);
        const compareResult2 = service.comparePropertyDes(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePropertyDes(entity1, entity2);
        const compareResult2 = service.comparePropertyDes(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
