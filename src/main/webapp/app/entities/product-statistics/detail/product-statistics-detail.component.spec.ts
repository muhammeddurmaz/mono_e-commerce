import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductStatisticsDetailComponent } from './product-statistics-detail.component';

describe('ProductStatistics Management Detail Component', () => {
  let comp: ProductStatisticsDetailComponent;
  let fixture: ComponentFixture<ProductStatisticsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductStatisticsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productStatistics: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProductStatisticsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductStatisticsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productStatistics on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productStatistics).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
