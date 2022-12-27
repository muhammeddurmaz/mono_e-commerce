import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../product.test-samples';

import { ProductFormService } from './product-form.service';

describe('Product Form Service', () => {
  let service: ProductFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductFormService);
  });

  describe('Service methods', () => {
    describe('createProductFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            barcode: expect.any(Object),
            modelCode: expect.any(Object),
            name: expect.any(Object),
            price: expect.any(Object),
            discountPrice: expect.any(Object),
            description: expect.any(Object),
            image: expect.any(Object),
            addedDate: expect.any(Object),
            rating: expect.any(Object),
            sizee: expect.any(Object),
            stock: expect.any(Object),
            active: expect.any(Object),
            category: expect.any(Object),
            subCategory: expect.any(Object),
            color: expect.any(Object),
            productModel: expect.any(Object),
            seller: expect.any(Object),
            brand: expect.any(Object),
          })
        );
      });

      it('passing IProduct should create a new form with FormGroup', () => {
        const formGroup = service.createProductFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            barcode: expect.any(Object),
            modelCode: expect.any(Object),
            name: expect.any(Object),
            price: expect.any(Object),
            discountPrice: expect.any(Object),
            description: expect.any(Object),
            image: expect.any(Object),
            addedDate: expect.any(Object),
            rating: expect.any(Object),
            sizee: expect.any(Object),
            stock: expect.any(Object),
            active: expect.any(Object),
            category: expect.any(Object),
            subCategory: expect.any(Object),
            color: expect.any(Object),
            productModel: expect.any(Object),
            seller: expect.any(Object),
            brand: expect.any(Object),
          })
        );
      });
    });

    describe('getProduct', () => {
      it('should return NewProduct for default Product initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createProductFormGroup(sampleWithNewData);

        const product = service.getProduct(formGroup) as any;

        expect(product).toMatchObject(sampleWithNewData);
      });

      it('should return NewProduct for empty Product initial value', () => {
        const formGroup = service.createProductFormGroup();

        const product = service.getProduct(formGroup) as any;

        expect(product).toMatchObject({});
      });

      it('should return IProduct', () => {
        const formGroup = service.createProductFormGroup(sampleWithRequiredData);

        const product = service.getProduct(formGroup) as any;

        expect(product).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProduct should not enable id FormControl', () => {
        const formGroup = service.createProductFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProduct should disable id FormControl', () => {
        const formGroup = service.createProductFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
