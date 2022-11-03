import { atom } from 'recoil'
import { recoilPersist } from 'recoil-persist'

export interface UserProps {
    id: number|null;
    name: string|null;
    account: string|null;
    email: string|null;
    bank: string|null;
}

const { persistAtom } = recoilPersist()

export const LoginedState = atom<boolean>({
    key: 'loginedState',
    default: false,
    effects_UNSTABLE: [persistAtom],
})

export const userState = atom<UserProps>({
        key: 'userState',
        default: {
            id: null,
            name: null,
            account: null,
            email: null,
            bank: null
        },
        effects_UNSTABLE: [persistAtom],
})