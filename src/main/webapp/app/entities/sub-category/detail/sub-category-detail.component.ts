import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubCategory } from '../sub-category.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-sub-category-detail',
  templateUrl: './sub-category-detail.component.html',
})
export class SubCategoryDetailComponent implements OnInit {
  subCategory: ISubCategory | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subCategory }) => {
      this.subCategory = subCategory;
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
