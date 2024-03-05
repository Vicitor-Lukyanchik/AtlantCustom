#INCLUDE "Box.ch"
#INCLUDE "Inkey.ch"
#INCLUDE "FileIo.ch"
#INCLUDE "Setcurs.ch"
#INCLUDE "MemoEdit.ch"

// #define K_CTRL_ENTER 10
#define K_VIEW K_ALT_S

#define ANY_CTRL   1
#define ANY_ALT    2
#define K_SH_INS   48

#define EOLN (chr(13)+chr(10))

MemVar br1,be1,br2,be2,br3,be3,br4,be4,t_uzel,t_vyb,bazam,nambas
MemVar _RO,DS,PS,SS,PAr,_OutKod,_OutPrim,DirPath,Level
MemVar View_All,Prim_All,HideOn,Razn_Mode,_OutArray
MemVar gr__,sg__,izd__
MemVar cond_blk,cond_add
MemVar parm
MemVar DataPath,p_cbr

Proc ee(ReadOnly,Number)
if Number # nil
  if len(Number)>1 .and. !Number$"12345"
    alert("Неправильные входные параметры.")
    QUIT
  endif
endif
SprMnu(DataPath[9]+"\",Number,ReadOnly)
Return

Func SprMnu(Path,Number,RO)
Local old_curs:=setcursor(),old_scr:=savescreen(0,0,24,79),old_clr:=setcolor()
Local Cont:=.t.,Ch,i:=0
Local Pos:=1,PredPos:=1
Local KodAr:={"",}
setcursor(SC_NONE)
SET SCOREBOARD OFF
SET DELETED ON
//SETDATE(NNETSDATE(),ISAT())
SET DATE TO GERMAN
if Number = nil
  dispbox(0,0,24,79,"         ","n/w*")
  dispbox(0,0,24,79,B_SINGLE,"n/w*")
  @  1, 40 say "С П Р А В О Ч Н И К И" color "r/w*"
  OneMem(1,0)
  for i:=2 to 6
    OneMem(0,i)
  next
  do while Cont
      Ch:=InKey(0)
      do case
        case Ch = K_UP
          Pos:=if(Pos # 1,--Pos,Pos)
          if PredPos # Pos
            OneMem(Pos,PredPos)
          endif
        case Ch = K_DOWN
          Pos:=if(Pos # 6,++Pos,Pos)
          if PredPos # Pos
            OneMem(Pos,PredPos)
          endif
        case Ch = K_ESC
          Cont:=.f.
        case Ch = K_ENTER
          if Pos<6
            KodAr:=KodOut__(str(Pos,1),RO,Path)
            if !empty(KodAr[1])
              Cont:=.f.
            endif
          else
            Cont:=.f.
          endif
      endcase
      PredPos:=Pos
  enddo
  setcolor(old_clr)
  restscreen(0,0,24,79,old_scr)
  setcursor(old_curs)
else
  KodAr:=KodOut__(Number,RO,Path)
  Cont:=.f.
endif
return (KodAr)


Func KodOut__(StrKod,ReadOnly,Path)
Local m1:={{"Группа","Комментарий"},{"&gr__->Name","&gr__->Komment"}},m2,m3,m4
Local SprAr:={{1,"Стандартные изделия","\SPEC\STAN\"},{2,"Материалы","\SPEC\MATE\"},{3,"Прочие изделия","\SPEC\RPRO\"},;
              {4,"Инстументы","\SPEC\INSTR\"},{5,"Остальные","\SPEC\REST\"}}
Local zag,put:=Path
Local old_scr:=savescreen(0,0,24,79)
Local CloseK,CloseP,CloseE,CloseF,CloseG,CloseGr,CloseSg,CloseIzd,ok,nHandle
Local old_ordP,old_ordE,old_ordF,old_ordG,old_ordGr,old_ordSg,old_ordIzd
Local old_recP,old_recE,old_recF,old_recG,old_recGr,old_recSg,old_recIzd
Private br1,be1,br2,be2,br3,be3,br4,be4,t_uzel:="",t_vyb:="",bazam,nambas
Private _RO:=.f.,DS:=-1,PS:=-1,SS:=-1,PAr[6],_OutArray:={"",},_OutKod:="",_OutPrim:="",DirPath:=put,Level:=1
Private HideOn:=.t.
Private cond_add,parm:=-1
Private gr__,sg__,izd__
private kod3_poisk,cur_vybor
Public  View_All:=.t.,Prim_All:=.f.,Razn_Mode:=.f.
if ReadOnly # nil .and. upper(ReadOnly) == "T"
  _RO:=.t.
elseif ReadOnly = nil
  _RO:=.f.
endif
DS:=-1
do case
   case StrKod=="1"
      bazam:=1
      m2:={{"Наименование","Масса","Обозначение","Код"},{"padr(vyvm1(&izd__->Name),68)","&izd__->Massa","&izd__->Obozn","left(&izd__->Name,15)"}}
      m3:={{"Пoдгруппа","Комментарий"},{"&sg__->Name","&sg__->Komment"}}
      m4:={{"Наименование","Обозначение","Кол-во","Масса"},{"vyvk(P->Kod_shto)","&izd__->Obozn","P->P_Kolvo","&izd__->Massa"}}
      nambas:="stan"
      gr__:=nambas+"1"
      sg__:=nambas+"3"
      izd__:=nambas+"2"
      zag:="стандартных"
      p_cbr:=14
      br4:=Crbr(9,1,22,78,m4)
      be4:={||P->Kod_kuda==t_uzel}
      br4:skipBlock    := {|x|P->(Skipped1(x,be4))}
      br4:goBottomBlock:= { ||P->(Skipped1(100000,be4))}
      br4:goTopBlock   := { ||P->(dbseek(t_uzel))}

   case StrKod=="2"
      bazam:=2
      m2:={{"Наименование","Масса","Предельн.откл.","Код"},{"padr(vyvm1(&izd__->Name),78)","&izd__->Massa","&izd__->Otkl","left(&izd__->Name,15)"}}
      m3:={{"Пoдгруппа","Комментарий"},{"&sg__->Name","&sg__->Komment"}}
      nambas:="mate"
      gr__:=nambas+"1"
      sg__:=nambas+"3"
      izd__:=nambas+"2"
      zag:="материалов"
   case StrKod=="3"
      bazam:=3
      m2:={{"Наименование","Наименование (русское)","Масса","Обозначение","Код"},{"padr(vyvm1(&izd__->Name),68)","&izd__->Name_rus","&izd__->Massa","&izd__->Obozn","left(&izd__->Name,15)"}}
      m3:={{"Пoдгруппа","Комментарий"},{"&sg__->Name","&sg__->Komment"}}
      nambas:="rpro"
      gr__:=nambas+"1"
      sg__:=nambas+"3"
      izd__:=nambas+"2"
      zag:="прочих"
   case StrKod=="4"
      bazam:=4
      m2:={{"Наименование","Обозначение","Сечение","Шир.канавки","Длина","Шаг резьбы","Диаметр","К.Морзе","Угол","Радиус","Резьба","Модуль","Шаг","Код"},;
        {"padr(vyvm1(&izd__->Name),68)","&izd__->Obozn","&izd__->Par1","&izd__->Par2","&izd__->Par3","&izd__->Par4","&izd__->Par5","&izd__->Par6","&izd__->Par7",;
         "&izd__->Par8","&izd__->Par9","&izd__->Par10","&izd__->Par11","left(&izd__->Name,15)"}}
      m3:={{"Пoдгруппа","Комментарий"},{"&sg__->Name","&sg__->Komment"}}
      nambas:="instr"
      gr__:=nambas+"1"
      sg__:=nambas+"3"
      izd__:=nambas+"2"
      zag:="инструмента"
   case StrKod=="5"
      bazam:=5
      m2:={{"Наименование","Масса","Код"},{"padr(vyvm1(&izd__->Name),78)","&izd__->Massa","left(&izd__->Name,15)"}}
      m3:={{"Пoдгруппа","Комментарий"},{"&sg__->Name","&sg__->Komment"}}
      nambas:="rest"
      gr__:=nambas+"1"
      sg__:=nambas+"3"
      izd__:=nambas+"2"
      zag:="остальных"

endcase
set defa to (put+nambas)
@ 0,0 say padr("  Т П П   ║ Ввод "+zag,80) COLOR("N/BG")
// showtime(0,72,,"n/bg")
//***********  1 -группа ****************************************
if !isOpen(gr__)
  if OpenDBF(.t.,"",nambas+"1",gr__,.t.,_RO)
    dbsetindex(nambas+"1")
  endif
  CloseGr:=.t.
else
  old_ordGr:=(gr__)->(indexord())
  old_recGr:=(gr__)->(recno())
  CloseGr:=.f.
endif
p_cbr:=14
br1:=(gr__)->(Crbr(2,1,22,78,m1,99)) // ,.t.)
// be1:={|| (gr__)->Prim=="1"}
br1:skipBlock    := {|x|(gr__)->(Skipped1(x,be1))}
br1:goBottomBlock:={|| (gr__)->(dbgoBottom()) }
br1:goTopBlock:={|| (gr__)->(dbgoTop()) }
// (gr__)->(Prim_OnOff(br1,1,.t.))

//***********  3 -подгруппа ****************************
if !isOpen(sg__)
  if OpenDBF(.t.,"",nambas+"3",sg__,.t.,_RO)
    dbsetindex(nambas+"3")
  endif
  CloseSg:=.t.
else
  old_ordSg:=(sg__)->(indexord())
  old_recSg:=(sg__)->(recno())
  CloseSg:=.f.
endif
p_cbr:=14
br3:=(sg__)->(Crbr(6,1,22,78,m3,99))
be3:={||(sg__)->Kod1==(gr__)->Kod}
br3:skipBlock    := {|x|(sg__)->(Skipped1(x,be3))}
// br3:goBottomBlock:= { ||(sg__)->(Skipped1(10000,be3))}
br3:goBottomBlock:= { ||(sg__)->(bottseek((gr__)->kod,be3))}
br3:goTopBlock   := { ||(sg__)->(dbseek((gr__)->Kod))}
// (sg__)->(Prim_OnOff(br3,2,.t.))
//***********  2 - изделие ****************************
if !isOpen(izd__)
  if OpenDBF(.t.,"",nambas+"2",izd__,.t.,_RO)
    dbsetindex(nambas+"2")
    if StrKod="4"
        dbsetindex(nambas+"n")
      else
        dbsetindex(nambas+"21")
    end
    dbsetindex(nambas+"22")
  endif
  CloseIzd:=.t.
else
  old_ordIzd:=(izd__)->(indexord())
  old_recIzd:=(izd__)->(recno())
  CloseIzd:=.f.
endif
p_cbr:=14
br2:=(izd__)->(Crbr(6,1,22,78,m2,98))
// be2:={|| (izd__)->Kod1+(izd__)->Kod3+(izd__)->Hide==(gr__)->Kod+(sg__)->Kod+"0"}
be2:={|| (izd__)->Kod1+(izd__)->Kod3==(gr__)->Kod+(sg__)->Kod}
br2:skipBlock    := {|x|(izd__)->(Skipped1(x,be2))}
// br2:goBottomBlock:= { ||(izd__)->(Skipped1(10000,be2))}
br2:goBottomBlock:= { ||(izd__)->(bottseek((gr__)->kod+(sg__)->kod,be2))}
br2:goTopBlock   := { ||(izd__)->(dbseek((gr__)->Kod+(sg__)->Kod))}
// (izd__)->(Prim_OnOff(br2,3,.t.))
/*
if StrKod="1"
   if !isOpen("E")
     if OpenDBF(.t.,put+"dbf\","e_e",'E',.t.,.t.)
       dbsetindex(put+"dbf\E_kod")
     endif
     CloseE:=.t.
   else
     old_ordE:=e->(indexord())
     old_recE:=e->(recno())
     e->(dbsetorder(1))  // kod
     CloseE:=.f.
   endif
elseif StrKod="3"
   if !isOpen("F")
     if OpenDBF(.t.,put+"dbf\","f_f",'F',.t.,.t.)
       dbsetindex(put+"dbf\f_kod")
     endif
     CloseF:=.t.
   else
     old_ordF:=f->(indexord())
     old_recF:=f->(recno())
     f->(dbsetorder(1))
     CloseF:=.f.
   endif
elseif StrKod="2"
   if !isOpen("G")
     if OpenDBF(.t.,put+"dbf\","g_g",'G',.t.,.t.)
       dbsetindex(put+"dbf\g_kod")
     endif
     CloseG:=.t.
   else
     old_ordG:=g->(indexord())
     old_recG:=g->(recno())
     g->(dbsetorder(1))
     CloseG:=.f.
   endif
endif
*/
//*************************************************************
/*
if isOpen("basa1") .and. isOpen("basa2") .and. isOpen("basa3")
  CloseK:=.f.
else
  CloseK:=.t.
  init_net_kodif(put+"bas\",,,_RO)
endif
*/
/*
if !file(DirPath+"BAS\spr_bas1.ntx") .or. !file(DirPath+"BAS\spr_bas2.ntx")
  dbusearea(.t.,,DirPath+"BAS\spr_bas","psw__",.f.,.f.)
  dbcreateindex(DirPath+"BAS\spr_bas1.ntx","charnot(ffio)",{|| charnot(psw__->ffio)})
  dbcreateindex(DirPath+"BAS\spr_bas2.ntx","fkod",{|| psw__->fkod })
  psw__->(dbclosearea())
endif
*/
OpenPsw()

DISPBOX(24,0,24,79," ","n/bg")
dispbox(1,0,23,79,B_DOUBLE,"w+/b")
StatusLn(1)
//trapshift("CtrlAltShift",15)
(gr__)->(mybrowse(br1,"user1__"))
//trapshift(,15)
// showtime()
restscreen(,,,,old_scr)
/*
do case
  case StrKod == "1"
      if isOpen("P") .and. CloseP
        p->(dbclosearea())
      else
        p->(dbsetorder(old_ordP))
        p->(dbgoto(old_recP))
      endif
      if isOpen("E")
        if CloseE
          E->(dbclosearea())
        else
          E->(dbsetorder(old_ordE))
          E->(dbgoto(old_recE))
        endif
      endif
  case StrKod == "2"
      if isOpen("G")
        if CloseG
          G->(dbclosearea())
        else
          G->(dbsetorder(old_ordG))
          G->(dbgoto(old_recG))
        endif
      endif
  case StrKod == "3"
      if isOpen("F")
        if CloseF
          F->(dbclosearea())
        else
          F->(dbsetorder(old_ordF))
          F->(dbgoto(old_recF))
        endif
      endif
endcase
*/
if isOpen("psw__")
  psw__->(dbclosearea())
endif
/*
if CloseK .and. isOpen("basa1")
  basa1->(dbclosearea())
endif
if CloseK .and. isOpen("basa2")
  basa2->(dbclosearea())
endif
if CloseK .and. isOpen("basa3")
  basa3->(dbclosearea())
endif
if CloseK .and. isOpen("basa4")
  basa4->(dbclosearea())
endif
*/
if CloseGr .and. isOpen(gr__)
  (gr__)->(dbclosearea())
endif
if CloseSg .and. isOpen(sg__)
  (sg__)->(dbclosearea())
endif
if CloseIzd .and. isOpen(izd__)
  (izd__)->(dbclosearea())
endif
return _OutArray

//*****************************************
func vyvk(kod)
(izd__)->(dbsetorder(2))
(izd__)->(dbseek(kod))
(izd__)->(dbsetorder(1))
return padr(vyvm1((izd__)->Name),55)
*************************************************************

STATIC Func IsOpen(alias)
Local Out:=.f.
if alias(select(upper(alias)))==upper(alias)
  Out:=.t.
endif
return (Out)

STATIC Func OneMem(Number,PredNum)
Local Y0:=1,X0:=4,dy:=2,dx:=20,ddy:=2
Local Color1:="w+/r"     //  Цвет выделенной
Local Color2             //  Цвет невыделенной
Local y:=0,x:=0,i:=0
Local Txt_Ar:={"Стандартные","Материалы","Комплектующие ","Инструмент","Остальные","Выход"}
Local Msg_Ar:={ {"  Работа со стандартными изделиями :",;
                 "",;
                 "   - болты, винты, пружины, шпонки, ..." ,;
                 "",;
                 "",;
                 "  При наличии доступа Вы можете редак-",;
                 " тировать состав и наименование групп, ",;
                 " подгрупп и изделий. ",;
                 "";
                },;
                {"  Материалы        :",;
                "",;
                 "   - балки, бумага, текстильн. изд., " ,;
                 "     профили,листы ...",;
                 "",;
                 "",;
                 "  При наличии доступа Вы можете редакти- ",;
                 " ровать состав и наименование групп, под-",;
                 " групп и изделий и изделий.";
                },;
                {"  Комплектующие  :",;
                 " (раздел спецификации <Прочие>)",;
                 "   - датчики, двигатели, зажимы, провод, ..." ,;
                 "",;
                 "",;
                 "  Название раздела соответствует соответ-",;
                 " ствующему разделу конструкторской специ-   ",;
                 " фикации.",;
                 "",;
                 "  При наличии доступа Вы можете редактиро- ",;
                 " вать состав и наименования групп, подгрупп" ,;
                 " и изделий."},;
                {"  Инструмент       :",;
                "",;
                 "   - зенкеры, зенковки, индикаторы, резцы," ,;
                 " сверла, фрезы, ...",;
                 "",;
                 "",;
                 "  При наличии доступа Вы можете редакти-  ",;
                 " ровать состав и наименование групп, под-",;
                 " групп и изделий.";
                },;
                {" Остальные : ",;
                 "",;
                 "",;
                 "Все изделия, не попавшие в предыдущие разделы",;
                 "",;
                 "",;
                 "  При наличии доступа Вы можете редакти-",;
                 "ровать состав и наименование групп, под-",;
                 "групп и изделий.";
                  },;
                {"",;
                 "",;
                 "" ,;
                 "",;
                 "",;
                 "          Выход из справочника.",;
                 "",;
                 "",;
                 "";
                } }
do case
  case PredNum=6
    Color2:="gr+/bg"
  otherwise
    Color2:="w+/b"
endcase
x:=x0+3
if Number >0
  dispbox(y:=y0+(Number-1)*(dy+ddy),x0,y+dy,x0+dx,"         ",Color1)
  @ y+1,x+2 say Txt_Ar[Number] color Color1
  dispbox(y0+3,x0+dx+6,y0+3+(dy+ddy)*3+dy,77,B_SINGLE+" ","bg+/b*")
  sha_shadow(y0+3,x0+dx+6,y0+3+(dy+ddy)*3+dy,77)
  for i:=1 to len(Msg_Ar[Number])
    @ y0+3+i,x0+dx+8 say Msg_Ar[Number,i] color "bg+/b*"
  next
endif
if PredNum>0
 dispbox(y:=y0+(PredNum-1)*(dy+ddy),x0,y+dy,x0+dx,"         ",Color2)
 @ y+1,x+2 say Txt_Ar[PredNum] color Color2
endif
return nil

STATIC Func StatusLn(Level,Mode)
Local old_clr:=setcolor()
Local Key:={{"Enter","Выбор","Level>0"},{"F6","Пров.код","Level=3"},{"F7","Поиск",".t."},{"F9","Печать","Level=3"} }
Local Key1:={{"Ctrl.. ","",".t."} }
Local Key2:={ {"Alt..  ","",".t."},{"A","Просм. всех","Level=1"},{"F","Кто внес",".t."},{"S","Полн. наим.","Level=3"},{"K","Вход в код-к","Level=1"}}  // ,{"R","Изм. примен.","PS<=Level .and. PS>=0 .and. Level>0"}
Local x:=col(),y:=row(),i
setcolor("rb/w*")
@ 24,0 say space(80) color "w/bg"
setpos(24,0)
if Mode = nil
  OutKey(Key)
elseif Mode = 1  // Ctrl
  OutKey(Key1)
elseif Mode = 2  // Alt
  OutKey(Key2)
endif
setpos(y,x)
setcolor(old_clr)
return nil

STATIC Proc OutKey(KeyAr)
Local len:=len(KeyAr),Ok,i
if KeyAr # nil .and. len(KeyAr)>0
  for i:=1 to len
     Ok:=.t.
     if KeyAr[i,3] # nil .and. !empty(KeyAr[i,3]) .and. valtype(KeyAr[i,3])=="C"
       if &(KeyAr[i,3])
       else
         Ok:=.f.
       endif
     endif
     if Ok
      @ row(),col()+1 say KeyAr[i,1] color "n/bg"
      @ row(),col() say (" "+KeyAr[i,2]) color "w+/bg"
     endif
  next
endif

Func User1__(browse,nKey)
Local old_scr,old_rec,old_ord
Local t_name,t_kod
do case
  case nKey = K_ENTER .and. !(gr__)->(eof())
    if browse:colPos = 2  // Комментарий
      if IsDostup(Level)
        (gr__)->(DoGet(browse))
      endif
    else
      old_scr:=savescreen(0,0,24,79)
      dispbox(1,0,4,79,B_DOUBLE+" ","n/n*")
      dispbox(5,0,23,79,B_DOUBLE,"w+/b")
      @ 2, 5 say "Группа  "+(gr__)->Name COLOR("w+/n*")
      Level++
      StatusLn(2)
   //   if(len(t_uzel)=0 .and. !Razn_Mode,(sg__)->(Prim_OnOff(br3,Level,.f.)),)
      (sg__)->(mybrowse(br3,"user3__"))
      Level--
   //   if(len(t_uzel)=0 .and. !Razn_Mode,(gr__)->(Prim_OnOff(br1,Level,.f.)),)
      if lastkey() = K_ESC // .and. !Razn_Mode
        setlastkey()
      endif
      restscreen(0,0,24,79,old_scr)
      if !Razn_Mode
        browse:refreshAll()
      endif
    endif

  case nKey = K_TAB .or. nKey = K_F10 .or. nKey = K_SH_TAB
    setlastkey()

//  case nKey = K_ALT_R .and. IsPrim(Level)
//    Prim_OnOff(browse,Level,.t.)

  case nKey = K_ALT_A
    Level--
    View_All()
    Level++

  case nKey = K_ALT_F
    WhoRec((gr__)->user)

  case nKey = K_F7 .and. !(gr__)->(eof())
    Poisk(browse)

  case nKey = K_F9
//    if isDostup(1) .and. alert("Распечатать новые ?",{"Да","Нет"})=1
//      PechNew(br2)
//    endif

//  case nKey = K_ALT_K
//    if alert("Хотите войти в кодировщик в режиме 'только для чтения' ? ",{"Да","Нет"}) = 1
//      vvodm0()
//    endif
endcase
return 0


Func User3__(browse,nKey)
Local old_scr,old_rec
Local t_name,t_kod
do case
  case nKey = K_ENTER .and. !(sg__)->(eof())
    if browse:colPos = 2  // Коментарий
      if isDostup(Level)
         (sg__)->(DoGet(browse))
         (sg__)->(dbcommit())
      endif
    else
      if !Razn_Mode
         old_scr:=savescreen(0,0,24,79)
         @ 3, 2 say "Подгруппа  "+substr((sg__)->Name,1,66) COLOR("w+/n*")
         dispbox(5,0,23,79,B_DOUBLE,"w+/b")
         Level++
         StatusLn(3)
   //      if(len(t_uzel)=0,(izd__)->(Prim_OnOff(br2,Level,.f.)),)
         (izd__)->(mybrowse(br2,"user2__"))
         Level--
   //      if(len(t_uzel)=0,(sg__)->(Prim_OnOff(br3,Level,.f.)),)
         if lastkey() = K_ESC
           setlastkey()
         endif
         restscreen(0,0,24,79,old_scr)
         if !Razn_Mode
           browse:refreshAll()
         endif
      endif
    endif

  case nKey = K_TAB .or. nKey = K_F10 .or. nKey = K_SH_TAB
    setlastkey()

//  case nKey = K_ALT_R .and. IsPrim(Level)
//    Prim_OnOff(browse,Level,.t.)

  case nKey = K_ALT_F
    WhoRec((sg__)->user)

  case nKey = K_F7 .and. !(sg__)->(eof())
    Poisk(browse)
endcase
return 0


Func User2__(browse,nKey)
Local old_scr,f_p_name:="fprim_"+str(bazam,1),i,j,old_br,St
do case
  case nKey = K_ENTER .and. !(izd__)->(eof())
    if browse:colPos=1 .and. len(t_uzel)=0
      if alert("Вы выбрали материал ?",{"Да","Нет"})=1 .and. ChoiceOk(izd__)
         _OutKod:=(izd__)->Name
         _OutArray:={_OutKod}
         keyboard (chr(K_ESC)+chr(K_ESC)+chr(K_ESC))
      endif
    elseif len(t_uzel)=0 .and.  browse:colPos>1 .and. IsDostup(3)
         (izd__)->(DoGet(browse))
//    elseif browse:colPos = 1 .and. len(t_uzel)>0 .and. alert("Вы выбрали вхождение",{"Нет","Да"})=2
//         t_vyb:=(izd__)->kod_name
//         keyboard (chr(K_ESC)+chr(K_ESC)+chr(K_ESC))
    endif

  case nKey = K_VIEW
    wivm((izd__)->Name)

//  case nKey = K_F3 .and. !(izd__)->(eof()) .and. len(t_uzel)=0 .and. upper(nambas)=="STAN" .and. !(izd__)->(P_ro(.t.))
//    if IsDostup(3,,(izd__)->user)
//      if alert("Будете работать с входимостью ?",{"Нет","Да"})=2
//        vhodim((izd__)->Kod_name)
//        browse:refreshAll()
//      endif
//    endif

//  case nKey = K_ALT_R .and. IsPrim(Level)
//    Prim_OnOff(browse,Level,.t.)

  case nKey = K_ALT_F
    WhoRec((izd__)->user,(izd__)->date,(izd__)->standart)

  case nKey = K_F7 .and. !(izd__)->(eof())
    Poisk(browse)

  case nKey = K_F6
/*
    if rtrim(substr((izd__)->name,17)) == (St:=rtrim(dekod1(substr((izd__)->name,1,15))))
      alert("Код соответствует наименованию.")
    else
      alert("Правильное наименование для этого кода : ;"+St)
    endif
*/

  case nKey = K_F9
    if isprinter() .and. alert("Печатать все изделия, входящие в подгруппу ? ",{"Нет","Да"}) = 2
      pech(browse)
    endif

endcase
return 0

function User4__(browse,nKey)
LOCAL   r_kod:="",old_rec
do case
    case nKey == K_ENTER.and.!P->(eof()).and.browse:colPos==3
        P->(DoGet(browse))
    case nKey == K_CTRL_INS
        if alert("Добавить запись в узел?",{"Нет","Да"})==2
            ap_vhodim()
            browse:refreshAll()
        end
    case nKey == K_CTRL_RET.and.!P->(eof())
        if !P->(eof()).and.alert("Редактировать запись в списке?",{"Нет","Да"})==2
            if P->(NetLock())
              ap_vhodim(.t.)
              browse:refreshAll()
            endif
        end
//    case nKey == K_CTRL_DEL.and.!P->(eof())     // удаление
//        if alert("Удалить текущую запись?",{"Нет","Да"})==2
//            if P->(NetLock())
//               P->(dbdelete())
//               P->(dbunlock())
//               P->(dbcommit())
//               browse:goTop()
//               browse:refreshAll()
//            endif
//        end
end
return 0

STATIC function vhodim(tkod)
LOCAL   old_rec1,old_rec2,old_rec3
t_uzel:=tkod
old_rec2:=(izd__)->(recno())
old_rec1:=(gr__)->(recno())
old_rec3:=(sg__)->(recno())
P->(mybrowse(br4,"user4__"))
setlastkey()
(izd__)->(dbgoto(old_rec2))
(gr__)->(dbgoto(old_rec1))
(sg__)->(dbgoto(old_rec3))
t_uzel:=""
return 0

STATIC function ap_vhodim(par)
LOCAL   old_rec,o2_rec,o3_rec,old_scr
Local Ok:=.t.
old_scr:=savescreen()
(gr__)->(mybrowse(br1,"User1__"))
if len(t_vyb)>0
    if par==NIL
        if !P->(NetAppend())
          Ok:=.f.
        endif
    end
    if Ok
      P->Kod_kuda:=t_uzel
      P->Kod_shto:=t_vyb
  //    P->Dlina:=(izd__)->Dlina
      P->User:="St"
      P->(dbunlock())
      P->(dbcommit())
    endif
end
setlastkey()
t_vyb:=""
restscreen(,,,,old_scr)
return 0


STATIC Func IsDostup(Level,OutScreen,user_kod)
Local Out:=.f.
Local TrueAlert:=.t.
if OutScreen # nil
 if !OutScreen
   TrueAlert:=.f.
 endif
endif
if _RO
  Out:=.f.
  if TrueAlert
    alert("Вы работаете в режиме 'только чтение'.")
  endif
elseif DS > Level .or. DS < 0
  Out:=.f.
  if TrueAlert
    alert("Нет доступа на этом уровне.")
  endif
elseif DS == 0
  Out:=.t.
elseif DS >0 .and. DS <= Level .and. user_kod # nil
   if user_kod == PAr[6]
     out:=.t.
   else
     if TrueAlert
       alert("Вы не можете удалить или отредактировать эту запись, ; т.к. ее ввел другой пользователь.")
     endif
   endif
else
  out:=.t.
endif
return Out


STATIC Func NetLock()
while !rlock()
  vyv_soob ("Нет возможности ИЗМЕНИТЬ запись!;F10-Выход" ,,"w+/r",,.f.)
  if inkey()=K_F10
     return .f.
  end
end
return .t.

STATIC Func NetAppend()
dbappend()
while neterr()
  vyv_soob ("Нет возможности добавить запись!;F10-Выход" ,,"w+/r",,.f.)
  if inkey()=K_F10
    return .f.
  end
  dbappend()
end
return .t.

STATIC Func OpenPsw()
Local ROMode:=!NetUserOk() .and. _RO .and. !(DS=0)
Local old_sel:=alias()
Local Ok:=.f.
if isOpen("psw__")
  psw__->(dbclosearea())
endif
if OpenDBF(.t.,DirPath+"BAS\","spr_bas","psw__",.t.,ROMode)
   dbsetindex(DirPath+"BAS\spr_bas1")
   dbsetindex(DirPath+"BAS\spr_bas2")
   Ok:=.t.
endif
if !empty(old_sel)
  dbselectarea(old_sel)
endif
return Ok


func nn_dlina(tnam,tdlin)
return if(tdlin==space(4),tnam,atrepl("ххх",tnam,alltrim(tdlin)))


STATIC func GetPrim()
Local StOut:="0",f_p_name:="fprim_"+str(bazam,1)
if !empty(select("psw__"))
  if !psw__->(eof())
    StOut:=charnot(psw__->&f_p_name)
    if StOut # "1"
      StOut:="0"
    endif
  endif
endif
return (StOut)

// права супервизора программы
STATIC Func NetUserOk()
/*
Local net_id:=upper(nnetwhoami())
Local net_ar:={{"Victor","Ira"},{"Oasup","Oasup"}}
Local Out:=.f.,Cont:=.t.,i:=1
do while Cont .and. i<=len(net_ar[1])
  Cont:=if((at(upper(net_ar[1,i]),net_id) > 0) .and. (at(upper(net_ar[2,i]),net_id) > 0),.f.,.t.)
  i++
enddo
Out:=!Cont
return (Out)
*/
return .f.

STATIC Func sha_attr(t,l,b,r,new_attr)
Local old_scr_area,new_scr_area,i
old_scr_area=savescreen(t,l,b,r)
new_scr_area=""
for i=1 to len(old_scr_area) step 2
  new_scr_area=new_scr_area+substr(old_scr_area,i,1)+chr(new_attr)
next
restscreen(t,l,b,r,new_scr_area)
return nil

#DEFINE ATTR_CONV(FORE,BACK)(BACK)*16+(FORE)
#define BLACK 0
#define WHITE 7
#define DK_GRAY 8
#define COL_SHADOW_ATTRIBUTE ATTR_CONV (DK_GRAY,BLACK)
#define BW_SHADOW_ATTRIBUTE ATTR_CONV (WHITE,BLACK)

STATIC Func sha_shadow(t,l,b,r)
Local shadow_attribute
//  Только если достаточно места
if r<maxcol() .and. b<maxrow()
  shadow_attribute=iif(iscolor(),COL_SHADOW_ATTRIBUTE,BW_SHADOW_ATTRIBUTE)
  sha_attr(b+1,l+1,b+1,r+1,shadow_attribute)
  sha_attr(t+1,r+1,b+1,r+1,shadow_attribute)
endif
RETURN NIL

STATIC Func OpenDBF(newreg,PATH,NAME,alia,sharedm,readm)
Local FPath:="",IsOpen:=.f.,Nn:=0,Cont:=.t.,new:=.t.
if !(PATH==NIL)
  FPATH:=PATH
endif
if newreg # nil .and. valtype(newreg)=="L"
 new:=newreg
endif
dbusearea(new,,FPATH+NAME,alia,sharedm,readm)
do while !IsOpen .and. Cont
  if !(IsOpen:=!neterr())
     Nn:=ALERT(" База данных "+FPATH+NAME+" занята другим пользователем. ; Повторить попытку открытия ? ",{"Да","Выход","Отмена"})
     do case
     case Nn==1
            &&  Повторение попытки открытия файла БД
       dbusearea(new,,FPATH+NAME,alia,sharedm,readm)
     case Nn==2
       QUIT       &&  Выход из программы с закрытием всех баз
     case Nn==3
       Cont:=.f.  &&  Выход из функции без открытия
     endcase
  else  //  База открыта успешно
    Cont:=.f.
  endif
enddo
RETURN IsOpen


STATIC funct vyv_soob(str,nom_str,color,ten,stop)
local old_bl,max_str,kol_str,i:=1,old:=setcolor(),cop,x1,x2,y1,y2,mas:={}
max_str:=1
if ten==nil
   ten=.t.
end
if stop==nil
   stop=.t.
end
if color==nil
   color="w+/b"
end
if str==nil
str="           "
end
setcursor(0)
kol_str:=numtoken(str,";")
for i:=1 to kol_str
 aadd(mas,token(str,";",i))
 if len(mas[i])>len(mas[max_str])
   max_str=i
 end
next
x1:=maxcol()/2-(len(mas[max_str])/2)-2
x2:=maxcol()/2+(len(mas[max_str])/2)+2
if nom_str==nil
   y1:=maxrow()/2-(kol_str/2)-1
   y2:=y1+kol_str+1
  else
   nom_str:=val(nom_str)
  y1:=if((nom_str+kol_str+1)<=maxrow() ,nom_str,maxrow()-kol_str-2)
  y2:=y1+kol_str+1
end
cop=savescreen(y1,x1,y2+1,x2+1)
setcolor(color)
dispbox(y1,x1,y2,x2,"╔═╗║╝═╚║ ",color)
if ten
  // colorwin()
  // teny({y1,x1,y2,x2})
end
for i:=1 to kol_str
   @ y1+i,x1+2 say mas[i]
next
if stop
  inkey(0)
  restscreen(y1,x1,y2+1,x2+1,cop)
end
setblink(old_bl)
setcolor(old)
return nil

/*
STATIC Func Hide_OnOff(Browse,Level,Change)
if Change # nil .and. Change
  HideOn := !HideOn
  @ 0,50 say if(HideOn,"Спр. вкл. ","Спр. выкл.") color "w+/bg"
endif
if HideOn
  be2:={||(izd__)->Kod1+(izd__)->Kod3+(izd__)->Hide==(gr__)->Kod+(sg__)->Kod+"0"}
  cond_blk:=be2
  browse:skipBlock:= {|x|(izd__)->(Skipped1(x,be2))}
  // browse:goBottomBlock:= { ||(izd__)->(Skipped1(10000,be2))}
  browse:goBottomBlock:= { ||(izd__)->(bottseek((gr__)->kod+(sg__)->kod+"0",be2))}
  browse:goTopBlock:= { ||(izd__)->(dbseek((gr__)->Kod+(sg__)->Kod+"0"))}
  br2:=browse
else
  be2:={||(izd__)->Kod1+(izd__)->Kod3==(gr__)->Kod+(sg__)->Kod}
  cond_blk:=be2
  browse:skipBlock:= {|x|(izd__)->(Skipped1(x,be2))}
//  browse:goBottomBlock:= { ||(izd__)->(Skipped1(10000,be2))}
  browse:goBottomBlock:= { ||(izd__)->(bottseek((gr__)->kod+(sg__)->kod,be2))}
  browse:goTopBlock:= { ||(izd__)->(dbseek((gr__)->Kod+(sg__)->Kod))}
  br2:=browse
endif
eval(browse:skipblock,0)
if !eval(be2) .or. eof()
  browse:goTop()
endif
browse:refreshAll()
return 0
*/

STATIC Func ChangePrim(Browse)
Local alia:=select(),prim_new
if (alia)->prim # "1"
  if alert("Желаете ввести в состав применяемости ?",{"Да","Нет"})==1
    prim_new:="1"
  endif
elseif (alia)->prim == "1"
  if alert("Желаете вывести из состава применяемости ?",{"Да","Нет"})==1
    prim_new:="0"
  endif
endif
if prim_new # nil
  if NetLock()
    (alia)->prim:=prim_new
    (alia)->(dbunlock())
    (alia)->(dbcommit())
    browse:refreshAll()
  endif
endif
return nil

Static Func View_All()
Local old_br2:={},old_scr
aadd(old_br2,br2:skipblock)
aadd(old_br2,br2:goBottomBlock)
aadd(old_br2,br2:goTopBlock)
br2:skipBlock:={|x| (izd__)->(Skipped1(x))}
br2:goBottomBlock:={|| (izd__)->(dbgoBottom())}
br2:goTopBlock:={|| (izd__)->(dbgoTop())}
old_scr:=savescreen(br2:nTop,br2:nLeft,br2:nBottom,br2:nRight)
(izd__)->(mybrowse(br2,"UserAll"))
br2:skipblock:=old_br2[1]
br2:goBottomBlock:=old_br2[2]
br2:goTopBlock:=old_br2[3]
restscreen(br2:nTop,br2:nLeft,br2:nBottom,br2:nRight,old_scr)
setlastkey()
return nil

Func UserAll(browse,nKey)
Local St
do case
 case nKey == K_F6
/*
   if rtrim(substr((izd__)->name,17)) == (St:=rtrim(dekod1(substr((izd__)->name,1,15))))
     alert("Код соответствует наименованию.")
   else
     alert("Правильное наименование для этого кода : ;"+St)
   endif
*/

 case nKey == K_F7
   fastFind(browse)
   // poisk(browse)
 case nKey == K_VIEW
   wivm((izd__)->Name)
 case nKey == K_ALT_N
   alert("Местонахождение изделия : ; Группа <"+alltrim(Group__((izd__)->kod1))+"> ; Подгруппа <"+alltrim(SGroup__((izd__)->kod1,(izd__)->kod3))+">")
 case nKey == K_ALT_F
   WhoRec((izd__)->user,(izd__)->date,(izd__)->standart)
endcase
return nil

Procedure CtrlAltShift()
Local old_scr:=savescreen(24,0,24,79)
if isbit(kbdstat(),3)  // Ctrl
  StatusLn(Level,ANY_CTRL)
elseif isbit(kbdstat(),4)  // Alt
  StatusLn(Level,ANY_ALT)
endif
do while numand(kbdstat(),15)>0 .and. nextkey()=0
enddo
restscreen(24,0,24,79,old_scr)

STATIC Func Group__(Kod)
Local old_rec:=(gr__)->(recno()),StOut:="? Kod="+Kod+" ?"
(gr__)->(dbgotop())
do while !((gr__)->kod == Kod) .and. !(gr__)->(eof())
  (gr__)->(dbskip())
enddo
if (gr__)->Kod==Kod
  StOut:=(gr__)->name
endif
(gr__)->(dbgoto(old_rec))
return (StOut)

Static Func SGroup__(Kodg,Kodsg)
Local old_rec:=(sg__)->(recno()),StOut:="? Kod="+Kodsg+" ?"
if (sg__)->(dbseek(Kodg))
   do while !((sg__)->kod == Kodsg) .and. (sg__)->kod1 == Kodg .and. !(sg__)->(eof())
     (sg__)->(dbskip())
   enddo
endif
if (sg__)->Kod==Kodsg .and. (sg__)->kod1==Kodg
  StOut:=(sg__)->name
endif
(sg__)->(dbgoto(old_rec))
return (StOut)

* Поиск изделия с учетом названия группы
*
 proc FastFind(browse)
local old_ord:=(izd__)->(indexord()),old_rec:=(izd__)->(recno())

set key K_INS to GroupCh
if !poisk(browse,,,cond_add)
  (izd__)->(dbgoto(old_rec))
endif
(izd__)->(dbsetorder(old_ord))
set key K_INS to
browse:refreshAll()

Proc GroupCh(cProc,nLine,cVar)
Local m1:={{"Группа"},{"(gr__)->Name"}}
Local brg,old_rec:=(gr__)->(recno()),nn:=0,name
Local gr_ch:={{"Для всех групп"},{nil}}
local old_scr:=savescreen(12,25,20,45)
(gr__)->(dbgotop())
(gr__)->(dbeval({|| aadd(gr_ch[1],(gr__)->name),aadd(gr_ch[2],(gr__)->kod)},,{|| !(gr__)->(eof())}))
(gr__)->(dbgoto(old_rec))
nn:=achoice(12,25,20,45,gr_ch[1])
if nn # 0
  if nn # 1
    cond_add:={|| (izd__)->kod1==gr_ch[2,nn]}
    (izd__)->(dbsetorder(1))
    if (izd__)->(dbseek(gr_ch[2,nn]))
      (izd__)->(dbskip(-1))
    endif
  else
    cond_add:=nil
  endif
endif
restscreen(12,25,20,45,old_scr)
if nn # 0
  name:=alltrim(gr_ch[1,nn])
  @ 12,64-len(name)-5 say "< "+name+">"
endif

static func pech(browse)
LOCAL   blkod,tskip:=1,col,i
eval(browse:goTopBlock)
blkod:=browse:skipBlock()
set cons off
set print on
?space(20)+(gr__)->Name
?space(20)+(sg__)->Name
?replicate("-",100)
while tskip>0
    ?padr(vyvm1((izd__)->Name),80)
    for i:=2 to browse:ColCount
        col:=browse:getColumn(i)
        ??eval(col:block)
    next
    tskip:=eval(blkod,1)
end
?replicate("-",100)
?
set cons on
set print off
return 0

*    Печать изменений
static Func PechNew(browse)
Local old_rec:=(izd__)->(recno()),old_ord:=(izd__)->(indexord())
Local f_name,nHandle
Local Ok:=.f.,St:="",i
Local get,getvar,getSt,nstrk:=0
Local zag,new_rec,scr_ar
do case
  case bazam == 1
    zag:="стандартных"
  case bazam == 2
    zag:="материалов"
  case bazam == 3
    zag:="прочих"
  case bazam == 4
    zag:="инструментов"
endcase
scr_ar:=OutMsg("Анализ изменений. ; Подождите.")
(izd__)->(dbsetorder(0))
(izd__)->(dbgoto((izd__)->(lastrec())))
do while (izd__)->(deleted()) .and. !(izd__)->(bof())
  (izd__)->(dbskip(-1))
enddo
do while !(izd__)->isprint .and. !(izd__)->(bof())
  (izd__)->(dbskip(-1))
enddo
if (izd__)->isprint
  (izd__)->(dbskip())
endif
restscreen(scr_ar[1],scr_ar[2],scr_ar[3],scr_ar[4],scr_ar[5])
if !(izd__)->isprint .and. !(izd__)->(eof())
  Ok:=.t.
  new_rec:=(izd__)->(recno())
else
  Ok:=.f.
  alert("Изменений в справочнике "+zag+" нет.")
endif
if Ok
    scr_ar:=OutMsg("Формирование ведомости изменений. ; Подождите.")
    f_name:=diskname()+":\"+curdir()+"\"+;
            left(nambas,4)+padl(ltrim(str(day(date()))),2,"0")+;
                  padl(ltrim(str(month(date()))),2,"0")+".txt"
    if file(f_name)
      nHandle:=FOPEN(f_name,FO_WRITE)
      if FERROR() >= 0
        FSEEK(nHandle,0,FS_END)
        Ok:=.t.
      else
        alert("Проблема с открытием файла новых "+zag+".; Повторите попытку позже.")
      endif
    else
      nHandle:=FCREATE(f_name,FC_NORMAL)
      if FERROR() < 0
        alert("Проблема с открытием файла новых мат-ов. ; Повторите попытку позже.")
      else
        Ok:=.t.
      endif
    endif
endif
if Ok
  if filesize(f_name) > 0
    St+=EOLN
  endif
  St:=replicate("=",80)+EOLN
  St+=time()+"                   Ведомость новых "+zag+EOLN
  nstrk:=1
  St+=shapka(browse)
  do while !(izd__)->(eof())
    if nstrk = 60
      St+=(chr(12))+EOLN
      St+=Shapka(browse)
      nstrk:=0
    endif
    for i:=1 to browse:colCount
      get:=browse:getColumn(i)
      getvar:=eval(get:block)
      do case
        case valtype(getvar) == "C"
          getst:=getvar
        case valtype(getvar) == "N"
          getst:=str(getvar)
        case valtype(getvar) == "L"
          getst:=padc(xtoc(getvar),get:width)
        otherwise
          if get:width # nil
            getst:=space(get:width)
          else
            getst:=space(8)
          endif
      endcase
      St+=+getst+"|"
    next
    St+=EOLN
    nstrk++
    if len(St)>10000
      FWRITE(nHandle,St)
      St:=""
    endif
    (izd__)->(dbskip())
  enddo
  FWRITE(nHandle,St)
  FCLOSE(nHandle)
  if FERROR() >= 0
    (izd__)->(dbgoto(new_rec))
    do while !(izd__)->(eof())
      if !(izd__)->isprint
        if (izd__)->(NetLock())
          (izd__)->isprint:=.t.
        endif
      endif
      (izd__)->(dbskip())
    enddo
    (izd__)->(dbunlock())
  endif
  restscreen(scr_ar[1],scr_ar[2],scr_ar[3],scr_ar[4],scr_ar[5])
  if FERROR() >= 0
    alert("Ведомость изменений "+zag+" в файле ;"+f_Name)
  endif
endif
(izd__)->(dbsetorder(old_ord))
(izd__)->(dbgoto(old_rec))
return nil

static func Shapka(browse)
Local St:="",get,i
  for i:= 1 to browse:colcount
    get:=browse:getColumn(i)
    St+=padc(get:heading,len(transCol(eval(get:block))))+"|"
  next
  St+=EOLN
return (St)

static func transCol(getvar)
Local getst
 do case
   case valtype(getvar) == "C"
     getst:=getvar
   case valtype(getvar) == "N"
     getst:=str(getvar)
   case valtype(getvar) == "L"
     getst:=padc(xtoc(getvar),5)
   otherwise
     getst:=space(8)
 endcase
return getst

Func OutMsg(St,y,ColorMsg,Shadow,Stop)
Local KolSt:=0,ArrSt:={},MaxSt:=1,MaxLen:=1
Local CurColor:="W+/bg"
Local old_scr,old_curs:=setcursor()
Local y1,x1,y2,x2,i
setcursor(SC_NONE)
KolSt:=numtoken(St,";")
for i:=1 to KolSt
  aadd(ArrSt,alltrim(token(St,";",i)))
  if len(ArrSt[i])>len(ArrSt[MaxSt])
    MaxSt:=i
  endif
next
MaxLen:=len(ArrSt[MaxSt])
if y # nil
  y1:=if(y+KolSt+1<maxrow(),y,int((maxrow()-KolSt)*0.5-1))
else
  y1:=int((maxrow()-KolSt)*0.5-1)
endif
y2:=y1+KolSt+1
x1:=int((maxcol()-len(ArrSt[MaxSt]))*0.5)-2
x2:=x1+len(ArrSt[MaxSt])+4
if ColorMsg # nil
  if valtype(ColorMsg)=="L"
    if ColorMsg
      CurColor:="w+/b"
    else
      CurColor:="w+/gr"
    endif
  elseif valtype(ColorMsg)=="C"
    CurColor:=ColorMsg
  endif
endif
old_scr:=savescreen(y1,x1,y2+1,x2+1)
dispbox(y1,x1,y2,x2,B_SINGLE+" ",CurColor)
for i:=1 to KolSt
  @ y1+i,x1+2 say padc(ArrSt[i],MaxLen) color CurColor
next
if Shadow # nil
  if valtype(Shadow) # "L" .or. (valtype(Shadow)=="L" .and. Shadow)
    sha_shadow(y1,x1,y2,x2)
  endif
else
  sha_shadow(y1,x1,y2,x2)
endif
if stop # nil
  inkey(0)
  setblink(old_blnk)
  setcursor(old_curs)
  restscreen(y1,x1,y2+1,x2+1,old_scr)
  return nil
else
  setblink(old_blnk)
  setcursor(old_curs)
  return {y1,x1,y2+1,x2+1,old_scr}
endif
return nil

Static Func WhoRec(UserKod,UserDate,Status)
Local old_ord:=psw__->(indexord()),old_rec:=psw__->(recno())
Local StatSt:='',DatSt:=''
if Status # nil
  do case
    case Status == '1'  // соотв. ГОСТу
      StatSt:='соотв. ГОСТу'
    case Status == '2'  // не соотв. ГОСТу
      StatSt:='не соотв. ГОСТу'
    case Status == '3'  // не исп-ть в КД
      StatSt:='не исп-ть в КД'
    case Status == '4'  // разовая
      StatSt:='разовая'
    otherwise           // непроверенная
      StatSt:='непроверенная'
  endcase
  StatSt:=';Статус : '+StatSt+'.'
endif
if UserDate # nil
  DatSt:=';Дата создания (редакт-ния) : '+dtoc(UserDate)
endif
psw__->(dbsetorder(2))  // fkod
if !empty(UserKod) .and. psw__->(dbseek(UserKod))
  alert("Эту запись создал или отредактировал последним ;"+charnot(psw__->ffio)+DatSt+StatSt)
  psw__->(dbgoto(old_rec))
else
  alert("Извините, неизвестно, кто создал эту запись."+DatSt+StatSt)
endif
psw__->(dbsetorder(old_ord))
return nil

//  Возможность выбора изделия из справочника материалов
func ChoiceOk(ali_)
local ok:=.f.
local ali
if ali_ # nil
  ali:=ali_
else
  ali:=izd__
endif
if !empty((ali)->standart)
  if (ali)->standart $ '134'  // соотвт- ГОСТу,  не исп-ся в КД,  либо отн-ся к разовым
    ok:=.t.
  else
    alert('Нельзя выбрать изделие несоответствующее ГОСТу. ; Для исправления записи обратитесь в бюро ; стандартов (тел. 5-50).')
  endif
else // возможность выбора непроверенных изделий
  ok:=.t.
endif
return ok

func ali(Ch)
local ch_:=substr(Ch,1,1)
return (ch_+'_'+ch_)
