import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ComputerComponent } from './list/computer.component';
import { ComputerDetailComponent } from './detail/computer-detail.component';
import { ComputerUpdateComponent } from './update/computer-update.component';
import { ComputerDeleteDialogComponent } from './delete/computer-delete-dialog.component';
import { ComputerRoutingModule } from './route/computer-routing.module';

@NgModule({
  imports: [SharedModule, ComputerRoutingModule],
  declarations: [ComputerComponent, ComputerDetailComponent, ComputerUpdateComponent, ComputerDeleteDialogComponent],
})
export class ComputerModule {}
