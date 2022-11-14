import { css } from '@emotion/react'
import { useRecoilValue } from 'recoil';
import MobileContainer from '../layout/container/mobileContainer';
import PageContainer from "../layout/container/pageContainer";
import { userState } from '../recoils/user';

const MyPageStyle = css`
`;

function MyPage(){
    const User = useRecoilValue(userState);
    console.log(User);
    return (
    	<MobileContainer>
        	<div css = {MyPageStyle}>
          		{User.name} <br />
          		{User.account} <br />
          		{User.bank}
        	</div>
      </MobileContainer>
	)
}

export default MyPage;