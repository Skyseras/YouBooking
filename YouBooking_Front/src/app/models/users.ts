import {Role} from "./role";
import {Reservation} from "./reservation";
import {Hotel} from "./hotel";

export class Users {
  id!:number;
  name!:String;
  username!:String;
  password!:String;
  appUserRoles!:Set<Role>;
  hotels!:Set<Hotel>;
  reservations!:Reservation[];
}
