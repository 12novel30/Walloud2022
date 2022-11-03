import { css } from '@emotion/react'
import PageContainer from "../layout/container/pageContainer";

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

function MainPage(){
  return (
      <div css = {blankError}>
        <div>
          로그인 후 메인 로비입니다.
        </div>
      </div>
  )
}

export default MainPage;