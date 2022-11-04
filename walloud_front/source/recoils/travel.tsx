import { atom } from 'recoil'
import { recoilPersist } from 'recoil-persist'

export interface TravelProps {
    travelId: number;
    name: string;
}

export interface EventProps {
    eventId: number;
    name: string;
    price: number;
    payerName: string;
    date: Date;
    isDetail: boolean;
    partiList: {eventRole: boolean,
                chargedPrice: number,
                name: string,
                personId: number}[];
}

const { persistAtom } = recoilPersist()

export const travelListState = atom<TravelProps[]>({
    key: "travelList",
    default: [],
    effects_UNSTABLE: [persistAtom]
})

export const eventListState = atom<EventProps[]>({
    key: "eventList",
    default: [],
    effects_UNSTABLE: [persistAtom]
})

export const personListState = atom<object[]>({
    key: "personList",
    default: [],
    effects_UNSTABLE: [persistAtom]
})

export const currentTravelState = atom<number|null>({
    key: "currentTravel",
    default: null,
    effects_UNSTABLE: [persistAtom],
})

