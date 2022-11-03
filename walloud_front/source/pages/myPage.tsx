import { css } from '@emotion/react'
import { useRecoilValue } from 'recoil';
import PageContainer from "../layout/container/pageContainer";
import { userState } from '../recoils/user';

const blankError = css`
  font-size: 40px;
  background-color: black;
  width: 100vw;
  height: 100vh;
  align-items: center;
  justify-content: center;
  display: flex;
  color: pink;
`;

function MyPage(){
    const User = useRecoilValue(userState);
    return (
      <div css = {blankError}>
        <div>
          {User.name} <br />
          {User.account} <br />
          {User.bank}
        </div>
      </div>
  )
}

export default MyPage;