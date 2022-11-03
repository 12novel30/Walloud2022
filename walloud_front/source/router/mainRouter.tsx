import { useRecoilValue } from 'recoil';
import { LoginedState, userState } from '../recoils/user';
import NotLoginedRouter from './notLoginedRouter';
import LoginedRouter from './LoginedRouter';
import { travelListState } from '../recoils/travel';

export default function MainRouter(){
    const isLogined = useRecoilValue(LoginedState)
    
    if (!isLogined){
        return <NotLoginedRouter />
    } else {
        return <LoginedRouter />
    }
}
