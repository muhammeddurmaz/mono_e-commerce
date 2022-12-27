import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductDiscount } from '../product-discount.model';

@Component({
  selector: 'jhi-product-discount-detail',
  templateUrl: './product-discount-detail.component.html',
})
export class ProductDiscountDetailComponent implements OnInit {
  productDiscount: IProductDiscount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productDiscount }) => {
      this.productDiscount = productDiscount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
