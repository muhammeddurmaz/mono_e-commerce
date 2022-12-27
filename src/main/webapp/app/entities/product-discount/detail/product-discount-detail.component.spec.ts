import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductDiscountDetailComponent } from './product-discount-detail.component';

describe('ProductDiscount Management Detail Component', () => {
  let comp: ProductDiscountDetailComponent;
  let fixture: ComponentFixture<ProductDiscountDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductDiscountDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productDiscount: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProductDiscountDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductDiscountDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productDiscount on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productDiscount).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
