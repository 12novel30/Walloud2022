import { atom } from 'recoil'
import { recoilPersist } from 'recoil-persist'

export interface TravelProps {
    travelId: number;
    name: string;
}

const { persistAtom } = recoilPersist()

export const travelListState = atom<TravelProps[]>({
    key: "travelList",
    default: [],
    effects_UNSTABLE: [persistAtom]
})

export const eventListState = atom<object[]>({
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

