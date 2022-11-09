import { atom } from "recoil";
import { recoilPersist } from "recoil-persist";

export interface TravelProps {
  travelId: number;
  name: string;
  //   imgSrc: string;
}
export interface EventProps {
  eventId: number;
  name: string;
  price: number;
  payerName: string;
  date: Date;
  isDetail: boolean;
  partiList: {
    eventRole: boolean;
    chargedPrice: number;
    name: string;
    personId: number;
  }[];
}

export interface PersonProps {
  personId: number;
  name: string;
  role: boolean;
  difference: number;
  userId: number;

  detail: {
    isView: boolean;
    isSettled: boolean;
    sumGet: number;
    sumSend: number;
    userAccount: string;
    userBank: string;
    eventList: any[];
  };
}

const { persistAtom } = recoilPersist();

export const travelListState = atom<TravelProps[]>({
  key: "travelList",
  default: [],
  effects_UNSTABLE: [persistAtom],
});

export const eventListState = atom<EventProps[]>({
  key: "eventList",
  default: [],
  effects_UNSTABLE: [persistAtom],
});

export const personListState = atom<PersonProps[]>({
  key: "personList",
  default: [],
  effects_UNSTABLE: [persistAtom],
});

export const currentTravelState = atom<number | null>({
  key: "currentTravel",
  default: null,
  effects_UNSTABLE: [persistAtom],
});
