import { atom } from 'recoil'
import { recoilPersist } from 'recoil-persist'

const { persistAtom } = recoilPersist()

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