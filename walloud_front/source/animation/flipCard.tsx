import { css, SerializedStyles } from "@emotion/react"
import Color from "../layout/globalStyle/globalColor";

const FlipCardStyle = css`
    perspective: 250px;
    &>div {
        height: 100%;
        width: 100%;
        backface-visibility: hidden;
        transition: ease 1s;
        &.front {
            position: absolute;
            transform: rotateY(0deg);
        }
        &.back {
            transform: rotateY(-180deg);
        }
    }
`

function FilpCard(props: any){
    return ( 
        <div css = {FlipCardStyle}>
            {props.children}
        </div>
    )
}

export default FilpCard;