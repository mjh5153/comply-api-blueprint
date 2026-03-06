import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'signals-demo-app';


  constructor() {

  }

//   public class UserComponent {

//     user$ = this.store.select(selectUser);



//     save(user: User) {

//         user$.take()

//         formGroup.get()
//         this.store.dispatch(updateUser({ user }));

//     }

//     let formGroup = new FormGroup({user: { name: ''}})
// }


// @if(user$ | async as user){

// <form (submit)="save(user)">
//     <input type="text"  [formGroup] />
//     <button type="submit">Save</button>
// </form>
// }
}
