import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoanNewComponent } from './loan-new.component';

describe('LoanNewComponent', () => {
  let component: LoanNewComponent;
  let fixture: ComponentFixture<LoanNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoanNewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoanNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
