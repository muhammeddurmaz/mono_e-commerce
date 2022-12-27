import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IColor } from '../color.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../color.test-samples';

import { ColorService } from './color.service';

const requireRestSample: IColor = {
  ...sampleWithRequiredData,
};

describe('Color Service', () => {
  let service: ColorService;
  let httpMock: HttpTestingController;
  let expectedResult: IColor | IColor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ColorService);
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

    it('should create a Color', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const color = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(color).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Color', () => {
      const color = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(color).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Color', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Color', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Color', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addColorToCollectionIfMissing', () => {
      it('should add a Color to an empty array', () => {
        const color: IColor = sampleWithRequiredData;
        expectedResult = service.addColorToCollectionIfMissing([], color);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(color);
      });

      it('should not add a Color to an array that contains it', () => {
        const color: IColor = sampleWithRequiredData;
        const colorCollection: IColor[] = [
          {
            ...color,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addColorToCollectionIfMissing(colorCollection, color);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Color to an array that doesn't contain it", () => {
        const color: IColor = sampleWithRequiredData;
        const colorCollection: IColor[] = [sampleWithPartialData];
        expectedResult = service.addColorToCollectionIfMissing(colorCollection, color);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(color);
      });

      it('should add only unique Color to an array', () => {
        const colorArray: IColor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const colorCollection: IColor[] = [sampleWithRequiredData];
        expectedResult = service.addColorToCollectionIfMissing(colorCollection, ...colorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const color: IColor = sampleWithRequiredData;
        const color2: IColor = sampleWithPartialData;
        expectedResult = service.addColorToCollectionIfMissing([], color, color2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(color);
        expect(expectedResult).toContain(color2);
      });

      it('should accept null and undefined values', () => {
        const color: IColor = sampleWithRequiredData;
        expectedResult = service.addColorToCollectionIfMissing([], null, color, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(color);
      });

      it('should return initial array if no Color is added', () => {
        const colorCollection: IColor[] = [sampleWithRequiredData];
        expectedResult = service.addColorToCollectionIfMissing(colorCollection, undefined, null);
        expect(expectedResult).toEqual(colorCollection);
      });
    });

    describe('compareColor', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareColor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareColor(entity1, entity2);
        const compareResult2 = service.compareColor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareColor(entity1, entity2);
        const compareResult2 = service.compareColor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareColor(entity1, entity2);
        const compareResult2 = service.compareColor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
