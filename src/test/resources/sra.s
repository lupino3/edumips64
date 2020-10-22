; sra.s - test file for EduMIPS64.
;
; Tests sra with several inputs.
;
; (c) 2020 Leopold Eckert and the EduMIPS64 team
;
; This file is part of the EduMIPS64 project, and is released under the GNU
; General Public License.
;
; This program is free software; you can redistribute it and/or modify
; it under the terms of the GNU General Public License as published by
; the Free Software Foundation; either version 2 of the License, or
; (at your option) any later version.
;
; This program is distributed in the hope that it will be useful,
; but WITHOUT ANY WARRANTY; without even the implied warranty of
; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
; GNU General Public License for more details.
;
; You should have received a copy of the GNU General Public License
; along with this program; if not, write to the Free Software
; Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
.data
    .word32 -2147483648
    .word32 1073741824

.code
    lw r1, 0(r0)
    sra r2, r1, 31
    addi r3, r0, -1
    bne r2, r3, error
    lw r1, 8(r0)
    sra r2, r1, 30
    addi r3, r0, 1
    bne r2, r3, error
    syscall 0

error:
    break
    syscall 0
