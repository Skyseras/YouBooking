import {StatusHotel} from "./status-hotel";
import {Users} from "./users";
import {Room} from "./room";

export class Hotel {
  private _id!:BigInt;
  private _name!:String;
  private _address!:String;
  private _city!:String;
  private _status!:StatusHotel;
  private _rooms!:Room[];
  private _manager!:Users;

  constructor(name: String, address: String, city: String, status: StatusHotel, rooms: Room[], manager: Users) {
    this._name = name;
    this._address = address;
    this._city = city;
    this._status = status;
    this._rooms = rooms;
    this._manager = manager;
  }


  get name(): String {
    return this._name;
  }

  set name(value: String) {
    this._name = value;
  }

  get address(): String {
    return this._address;
  }

  set address(value: String) {
    this._address = value;
  }

  get city(): String {
    return this._city;
  }

  set city(value: String) {
    this._city = value;
  }

  get status(): StatusHotel {
    return this._status;
  }

  set status(value: StatusHotel) {
    this._status = value;
  }

  get rooms(): Room[] {
    return this._rooms;
  }

  set rooms(value: Room[]) {
    this._rooms = value;
  }

  get manager(): Users {
    return this._manager;
  }

  set manager(value: Users) {
    this._manager = value;
  }

  get id(): BigInt {
    return this._id;
  }

}
