import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductTypeDetailComponent } from './product-type-detail.component';

describe('ProductType Management Detail Component', () => {
  let comp: ProductTypeDetailComponent;
  let fixture: ComponentFixture<ProductTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProductTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
