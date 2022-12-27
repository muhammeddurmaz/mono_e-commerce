import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductInventoryDetailComponent } from './product-inventory-detail.component';

describe('ProductInventory Management Detail Component', () => {
  let comp: ProductInventoryDetailComponent;
  let fixture: ComponentFixture<ProductInventoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductInventoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productInventory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProductInventoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductInventoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productInventory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productInventory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
