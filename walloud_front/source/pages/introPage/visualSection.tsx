import { css } from '@emotion/react'
import Color from '../../layout/globalStyle/globalColor';

const visualSection = css`
  background-color: ${Color.blueClassic};
  color: white;
  font-size: 20px;
`;

function VisualSection(){
    return (
        <div css = {visualSection}>
            모임을 가진 후, 정산이 오래 걸리지는 않았나요?<br />
            <br />

            Walloud에서 한 번에 해결하세요
        </div>
    )
}

export default VisualSection;