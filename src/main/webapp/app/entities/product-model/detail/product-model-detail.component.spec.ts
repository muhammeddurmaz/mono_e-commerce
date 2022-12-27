import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductModelDetailComponent } from './product-model-detail.component';

describe('ProductModel Management Detail Component', () => {
  let comp: ProductModelDetailComponent;
  let fixture: ComponentFixture<ProductModelDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductModelDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productModel: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProductModelDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductModelDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productModel on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productModel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
