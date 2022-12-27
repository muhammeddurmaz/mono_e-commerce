import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../product-model.test-samples';

import { ProductModelFormService } from './product-model-form.service';

describe('ProductModel Form Service', () => {
  let service: ProductModelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductModelFormService);
  });

  describe('Service methods', () => {
    describe('createProductModelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductModelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            modelCode: expect.any(Object),
          })
        );
      });

      it('passing IProductModel should create a new form with FormGroup', () => {
        const formGroup = service.createProductModelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            modelCode: expect.any(Object),
          })
        );
      });
    });

    describe('getProductModel', () => {
      it('should return NewProductModel for default ProductModel initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createProductModelFormGroup(sampleWithNewData);

        const productModel = service.getProductModel(formGroup) as any;

        expect(productModel).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductModel for empty ProductModel initial value', () => {
        const formGroup = service.createProductModelFormGroup();

        const productModel = service.getProductModel(formGroup) as any;

        expect(productModel).toMatchObject({});
      });

      it('should return IProductModel', () => {
        const formGroup = service.createProductModelFormGroup(sampleWithRequiredData);

        const productModel = service.getProductModel(formGroup) as any;

        expect(productModel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductModel should not enable id FormControl', () => {
        const formGroup = service.createProductModelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductModel should disable id FormControl', () => {
        const formGroup = service.createProductModelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
