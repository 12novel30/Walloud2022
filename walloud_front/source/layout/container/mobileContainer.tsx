import { css } from '@emotion/react'
import Color from '../globalStyle/globalColor';

const MobileContainerStyle = css`
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100vw;
    height: 100vh;
    background-color: ${Color.blue02};
    // background-image: url();
    background-size: cover;
    background-repeat: no-repeat;
    gap: 20px;
` 
function MobileContainer(props: any){
  return (
    <div css = {MobileContainerStyle}>
        {props.children}
    </div>
  )
}

export default MobileContainer;