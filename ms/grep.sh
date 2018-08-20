#!/bin/bash

IFS=$'\n'

AMDOCS_F="amdocs"
ATTUIDS_F="attuids"
MECHIDS_F="mechids"
ATT_F="att"
COMATT_F="comatt"
ATTCOM_F="attcom"
ATTWS_F="attws"
PUBLIC135_F="public135"
PUBLIC12_F="public12"
NOD_F="networkOnDemand"
AIC_F="aic"
UCPE_F="ucpe"
ATNT_F="atandt"
COMMERCIAL_F="commercial"
FILES_ARRAY=( "$AMDOCS_F" "$ATTUIDS_F" "$MECHIDS_F" "$ATT_F" "$COMATT_F" "$ATTCOM_F" "$PUBLIC135_F" "$PUBLIC12_F" "$NOD_F" "$AIC_F" "$UCPE_F" "$ATNT_F" "$COMMERCIAL_F" "$ATT_WS" )

EXTENSIONS_EXCLUDED="*.png,*.gif,*.jpg,*.jpeg,*.svg,*.bmp"

CONTEXT=".{0,30"

EXCLUDE_F="{grep\.sh"
for FILE in "${FILES_ARRAY[@]}"
do
    EXCLUDE_F="$EXCLUDE_F,$FILE\.txt"
done
EXCLUDE_F="$EXCLUDE_F,$EXTENSIONS_EXCLUDED}"

EXCLUDE_DIR="\.git"
GREP_EXCLUDE="--exclude=$EXCLUDE_F --exclude-dir=\"$EXCLUDE_DIR\""
PATTERNS_EXCLUDED=( "pattern" "attrib" "attach" "[hH]eat[tT]" "[Ff]ormatt" "attempt" "[Ff]loatT" "[Mm]atter " "[Ll]atter " )

REPO="./"
PUBLISH_DIR="publish"
NEW_DIR="$PUBLISH_DIR/$BUILD_TAG/new"
OLD_DIR="$PUBLISH_DIR/$BUILD_TAG/old"
DIFF_DIR="$PUBLISH_DIR/$BUILD_TAG/diff"

HEADER="This file was generated with"
LINE="===================================="

function scan {
    GREP=$1
    FILE=$2
    OUTPUT=$3
    echo "PROCESSING $GREP FOR FILE $FILE"
    echo "$HEADER $GREP"        >  "$OUTPUT/$FILE.txt"
    echo "$LINE"                >> "$OUTPUT/$FILE.txt"
    eval "$GREP" "$REPO" | sort >> "$OUTPUT/$FILE.txt"
}

function scanAll {
    OUTPUT=$1
    echo "SCANNING FOR $OUTPUT"
    scan "grep -riHoE \"$CONTEXT}amdocs$CONTEXT}\" $GREP_EXCLUDE"                                            "$AMDOCS_F"     "$OUTPUT"
    scan "grep -rHoE \"$CONTEXT}[a-zA-Z][a-zA-Z][0-9][0-9][0-9][0-9a-zA-Z]$CONTEXT}\" $GREP_EXCLUDE"         "$ATTUIDS_F"    "$OUTPUT"
    scan "grep -riHoE \"$CONTEXT}[mgtecv][0-9][0-9][0-9][0-9][0-9]$CONTEXT}\" $GREP_EXCLUDE"                         "$MECHIDS_F"    "$OUTPUT"
    scan "grep -riHoE \"$CONTEXT}att$CONTEXT}\" $GREP_EXCLUDE"                                               "$ATT_F"        "$OUTPUT"
    scan "grep -riHoE \"$CONTEXT}com\.att$CONTEXT}\" $GREP_EXCLUDE"                                          "$COMATT_F"     "$OUTPUT"
    scan "grep -riHoE \"$CONTEXT}att\.com$CONTEXT}\" $GREP_EXCLUDE"                                          "$ATTCOM_F"     "$OUTPUT"
    scan "grep -riHoE \"$CONTEXT}attws\.com$CONTEXT}\" $GREP_EXCLUDE"                                        "$ATTWS_F"      "$OUTPUT"
    scan "grep -rEo '$CONTEXT}135\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$CONTEXT}' $GREP_EXCLUDE"            "$PUBLIC135_F"  "$OUTPUT"
    scan "grep -rEo '$CONTEXT}12\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$CONTEXT}' $GREP_EXCLUDE"             "$PUBLIC12_F"   "$OUTPUT"
    scan "grep -riHoE '$CONTEXT}Network On Demand$CONTEXT}' $GREP_EXCLUDE"                                   "$NOD_F"        "$OUTPUT"
    scan "grep -riHoE \"$CONTEXT}aic$CONTEXT}\" $GREP_EXCLUDE"                                               "$AIC_F"        "$OUTPUT"
    scan "grep -riHoE \"$CONTEXT}ucpe$CONTEXT}\" $GREP_EXCLUDE"                                              "$UCPE_F"       "$OUTPUT"
    scan "grep -riHoE \"$CONTEXT}at&t$CONTEXT}\" $GREP_EXCLUDE"                                              "$ATNT_F"       "$OUTPUT"
    scan "grep -riHoE \"$CONTEXT}commercial$CONTEXT}\" $GREP_EXCLUDE"                                        "$COMMERCIAL_F" "$OUTPUT"
}

function diff {
    FILENAME=$1
    echo "DOING DIFF FOR $1"
    echo "ADDED FROM $BASE_CHECKOUT"                    >   "$DIFF_DIR/$FILENAME"
    echo "$LINE"                                        >>  "$DIFF_DIR/$FILENAME"
    grep -vFf "$OLD_DIR/$FILENAME" "$NEW_DIR/$FILENAME" >>  "$DIFF_DIR/$FILENAME"
}

function patternExclusions {
    OUTPUT=$1
    for EXCLUSION in "${PATTERNS_EXCLUDED[@]}"
    do
        echo "EXCLUDING $EXCLUSION FROM $OUTPUT/$ATT_F.txt"
        sed -i "s/$EXCLUSION//gI" "$OUTPUT/$ATT_F.txt"
        sed -i "/[aA][tT][tT]/!d" "$OUTPUT/$ATT_F.txt"
    done
}

echo "Creating folders"
mkdir -p "$NEW_DIR"
mkdir -p "$OLD_DIR"
mkdir -p "$DIFF_DIR"

echo "Scanning new version :"
git log -1
git log -1 > "$NEW_DIR/$VERSION_FILE"
scanAll "$NEW_DIR"
patternExclusions "$NEW_DIR"

git checkout "$BASE_CHECKOUT"

echo "Scanning old version $BASE_CHECKOUT :"
git log -1
git log -1 > "$OLD_DIR/$VERSION_FILE"
scanAll "$OLD_DIR"
patternExclusions "$OLD_DIR"

echo "produicing the diff"
for FILE in "${FILES_ARRAY[@]}"
do
    diff "$FILE.txt"
done
