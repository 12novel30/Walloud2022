import { Route, Routes } from 'react-router-dom'
import TravelMainPage from '../pages/dividePage/travelMainPage';
import TravelPage from '../pages/dividePage/travelPage';
import MainPage from '../pages/mainPage';
import MyPage from '../pages/myPage';
import NotFound from '../pages/notFound';

function LoginedRouter() {
    return (
        <Routes>
            <Route path="/" element = {<MainPage />}/>
            <Route path="/travel" element ={<TravelMainPage />} />
            <Route path="/travel/:travelName" element ={<TravelPage />} />
            <Route path="/mypage" element ={<MyPage />} />
            <Route path='/*' element = {<NotFound />} />
        </Routes>
    )
}

export default LoginedRouter;
