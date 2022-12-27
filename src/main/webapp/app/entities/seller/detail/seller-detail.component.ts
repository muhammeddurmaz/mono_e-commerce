import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISeller } from '../seller.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-seller-detail',
  templateUrl: './seller-detail.component.html',
})
export class SellerDetailComponent implements OnInit {
  seller: ISeller | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ seller }) => {
      this.seller = seller;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
