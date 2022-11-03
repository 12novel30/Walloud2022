import { css } from "@emotion/react"
import { Link } from "react-router-dom"
import { useRecoilValue, useResetRecoilState } from "recoil"
import { ScreenSize } from "../../layout/globalStyle/globalSize"
import { eventListState } from "../../recoils/travel"
import { LoginedState } from "../../recoils/user"

const NavMenuWrapper = css`
    float: right;
    gap: 10px;
    flex-grow: 1;
    font-size: 18px;
    &>a {
        padding: 0px 7px;
        border-right: 5px solid;
    }
    @media (max-width: ${ScreenSize.tablet}) {
        width: 0px;
        visibility: hidden;
    }
`

function NavMenu(){
    return (
        <div css = {NavMenuWrapper}>
            {!useRecoilValue(LoginedState) ?
            <><Link to="/divide">DivideByN</Link>
            <Link to="/about">About Us</Link></>
            : <><Link to="/book">Account Book</Link>
            <Link to="/feed">Feed</Link>
            <Link to="/travel">Travel</Link></>}
        </div>
    )
}

export default NavMenu;