import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { UserAdressFormService, UserAdressFormGroup } from './user-adress-form.service';
import { IUserAdress } from '../user-adress.model';
import { UserAdressService } from '../service/user-adress.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-user-adress-update',
  templateUrl: './user-adress-update.component.html',
})
export class UserAdressUpdateComponent implements OnInit {
  isSaving = false;
  userAdress: IUserAdress | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: UserAdressFormGroup = this.userAdressFormService.createUserAdressFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected userAdressService: UserAdressService,
    protected userAdressFormService: UserAdressFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAdress }) => {
      this.userAdress = userAdress;
      if (userAdress) {
        this.updateForm(userAdress);
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userAdress = this.userAdressFormService.getUserAdress(this.editForm);
    if (userAdress.id !== null) {
      this.subscribeToSaveResponse(this.userAdressService.update(userAdress));
    } else {
      this.subscribeToSaveResponse(this.userAdressService.create(userAdress));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserAdress>>): void {
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

  protected updateForm(userAdress: IUserAdress): void {
    this.userAdress = userAdress;
    this.userAdressFormService.resetForm(this.editForm, userAdress);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, userAdress.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.userAdress?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
