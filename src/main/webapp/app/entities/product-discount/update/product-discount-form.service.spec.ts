import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../product-discount.test-samples';

import { ProductDiscountFormService } from './product-discount-form.service';

describe('ProductDiscount Form Service', () => {
  let service: ProductDiscountFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductDiscountFormService);
  });

  describe('Service methods', () => {
    describe('createProductDiscountFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductDiscountFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            addedDate: expect.any(Object),
            dueDate: expect.any(Object),
            description: expect.any(Object),
            discount: expect.any(Object),
            product: expect.any(Object),
          })
        );
      });

      it('passing IProductDiscount should create a new form with FormGroup', () => {
        const formGroup = service.createProductDiscountFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            addedDate: expect.any(Object),
            dueDate: expect.any(Object),
            description: expect.any(Object),
            discount: expect.any(Object),
            product: expect.any(Object),
          })
        );
      });
    });

    describe('getProductDiscount', () => {
      it('should return NewProductDiscount for default ProductDiscount initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createProductDiscountFormGroup(sampleWithNewData);

        const productDiscount = service.getProductDiscount(formGroup) as any;

        expect(productDiscount).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductDiscount for empty ProductDiscount initial value', () => {
        const formGroup = service.createProductDiscountFormGroup();

        const productDiscount = service.getProductDiscount(formGroup) as any;

        expect(productDiscount).toMatchObject({});
      });

      it('should return IProductDiscount', () => {
        const formGroup = service.createProductDiscountFormGroup(sampleWithRequiredData);

        const productDiscount = service.getProductDiscount(formGroup) as any;

        expect(productDiscount).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductDiscount should not enable id FormControl', () => {
        const formGroup = service.createProductDiscountFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductDiscount should disable id FormControl', () => {
        const formGroup = service.createProductDiscountFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
