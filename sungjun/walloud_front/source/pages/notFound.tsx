import { css } from '@emotion/react'

const blankError = css`
  font-size: 40px;
  background-color: black;
  width: 100vw;
  height: 100vh;
  align-items: center;
  justify-content: center;
  display: flex;
  color: yellow;
`;

function NotFound(){
  return (
      <div css = {blankError}>
        <div>
          비정상적인 접근입니다.
        </div>
      </div>
  )
}

export default NotFound;