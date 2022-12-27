import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SellerFormService, SellerFormGroup } from './seller-form.service';
import { ISeller } from '../seller.model';
import { SellerService } from '../service/seller.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProductType } from 'app/entities/product-type/product-type.model';
import { ProductTypeService } from 'app/entities/product-type/service/product-type.service';

@Component({
  selector: 'jhi-seller-update',
  templateUrl: './seller-update.component.html',
})
export class SellerUpdateComponent implements OnInit {
  isSaving = false;
  seller: ISeller | null = null;

  usersSharedCollection: IUser[] = [];
  productTypesSharedCollection: IProductType[] = [];

  editForm: SellerFormGroup = this.sellerFormService.createSellerFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected sellerService: SellerService,
    protected sellerFormService: SellerFormService,
    protected userService: UserService,
    protected productTypeService: ProductTypeService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareProductType = (o1: IProductType | null, o2: IProductType | null): boolean => this.productTypeService.compareProductType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ seller }) => {
      this.seller = seller;
      if (seller) {
        this.updateForm(seller);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('fastShopApp.error', { message: err.message })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const seller = this.sellerFormService.getSeller(this.editForm);
    if (seller.id !== null) {
      this.subscribeToSaveResponse(this.sellerService.update(seller));
    } else {
      this.subscribeToSaveResponse(this.sellerService.create(seller));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISeller>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(seller: ISeller): void {
    this.seller = seller;
    this.sellerFormService.resetForm(this.editForm, seller);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, seller.user);
    this.productTypesSharedCollection = this.productTypeService.addProductTypeToCollectionIfMissing<IProductType>(
      this.productTypesSharedCollection,
      seller.sellerProductType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.seller?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.productTypeService
      .query()
      .pipe(map((res: HttpResponse<IProductType[]>) => res.body ?? []))
      .pipe(
        map((productTypes: IProductType[]) =>
          this.productTypeService.addProductTypeToCollectionIfMissing<IProductType>(productTypes, this.seller?.sellerProductType)
        )
      )
      .subscribe((productTypes: IProductType[]) => (this.productTypesSharedCollection = productTypes));
  }
}
