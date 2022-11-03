import { css } from '@emotion/react'
import ScrollNavi from '../../animation/scrollNavi';
import Color from '../../layout/globalStyle/globalColor';
import Logo from '../image/logo';
import NavMenu from './navMenu';
import NavUtil from './navUtil';

const navStyle = css`
    position: fixed;
    left: 0;
    top: 0;
    height: 70px;
    width: 100%;
    padding: 5px 0px;
    display: inline-flex;
    z-index: 1000;
    transition: 1s ease;
    align-items: center;
    &>* {
        display: inline-flex;
        align-items: center;
        padding: 0 20px;
        &>img {
            filter: invert()
        }
        &>a {
            color: white;
            &:hover {
                color: ${Color.gray05};
            }
        }
    }
    `
let navMode: {[index: string]:any} = {
    'top': css` 
        background-color: transparent;
    `,
    'down': css`
        position: fixed;
        top: -80px;
    `,
    'up': css`
        background-color: white;
        &>* {
            &>img {
                filter: none;
            }
            &>a {
                color: black;
            }
        } 
    `,
}

function NavBar(){
    let screenMode = ScrollNavi();
    return (
    <div css = {[navStyle, navMode[screenMode]]}>
        {Logo('70px', '')}
        <NavMenu />
        <NavUtil />
    </div>
    );
}


export default NavBar;