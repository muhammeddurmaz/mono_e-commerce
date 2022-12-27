import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductType } from '../product-type.model';

@Component({
  selector: 'jhi-product-type-detail',
  templateUrl: './product-type-detail.component.html',
})
export class ProductTypeDetailComponent implements OnInit {
  productType: IProductType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productType }) => {
      this.productType = productType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
